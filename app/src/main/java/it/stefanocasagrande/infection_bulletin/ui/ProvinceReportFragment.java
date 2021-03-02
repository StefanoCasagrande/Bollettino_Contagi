package it.stefanocasagrande.infection_bulletin.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.Date;

import it.stefanocasagrande.infection_bulletin.Common.Common;
import it.stefanocasagrande.infection_bulletin.Covid_Interface;
import it.stefanocasagrande.infection_bulletin.GlobalVariables;
import it.stefanocasagrande.infection_bulletin.MainActivity;
import it.stefanocasagrande.infection_bulletin.R;
import it.stefanocasagrande.infection_bulletin.json_classes.reports.Data_Reports;
import it.stefanocasagrande.infection_bulletin.json_classes.reports.Province_Response;
import it.stefanocasagrande.infection_bulletin.json_classes.reports.Total_Response;

import static it.stefanocasagrande.infection_bulletin.Common.Common.AddDotToInteger;
import static it.stefanocasagrande.infection_bulletin.Common.Common.hideKeyboard;

public class ProvinceReportFragment extends Fragment implements Covid_Interface, View.OnClickListener{

    TextView tv_date;
    TextView tv_confirmed_cases;
    TextView tv_confirmed_diff;
    TextView tv_deaths;
    TextView tv_deaths_diff;
    TextView tv_recovered;
    TextView tv_recovered_diff;
    TextView tv_active;
    TextView tv_active_diff;
    TextView tv_fatality_rate;
    TextView tv_region;

    static String iso;
    static Date selected_date;
    static String province;
    static String name;

    public ProvinceReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (Common.Database.Bookmark_Select(getActivity(), iso, province, name, "BOOKMARK").size()>0) {
            menu.findItem(R.id.remove_bookmark).setVisible(true);
            menu.findItem(R.id.save_bookmark).setVisible(false);
        }
        else {
            menu.findItem(R.id.remove_bookmark).setVisible(false);
            menu.findItem(R.id.save_bookmark).setVisible(true);
        }

        if (Common.Database.Bookmark_Select(getActivity(), iso, province, name, "HOME").size()>0) {
            menu.findItem(R.id.remove_home).setVisible(true);
            menu.findItem(R.id.save_home).setVisible(false);
        }
        else {
            menu.findItem(R.id.remove_home).setVisible(false);
            menu.findItem(R.id.save_home).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_bookmark:

                if (Common.Database.Bookmark_Save(iso, province, name, "BOOKMARK"))
                    Toast.makeText(getActivity(), getString(R.string.Operation_Success), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.Operation_Failed), Toast.LENGTH_LONG).show();

                return true;

            case R.id.remove_bookmark:

                if (Common.Database.Bookmark_Remove(iso, province, name, "BOOKMARK"))
                    Toast.makeText(getActivity(), getString(R.string.Operation_Success), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.Operation_Failed), Toast.LENGTH_LONG).show();

                return true;

            case R.id.save_home:

                if (Common.Database.Bookmark_Save(iso, province, name, "HOME"))
                    Toast.makeText(getActivity(), getString(R.string.Operation_Success), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.Operation_Failed), Toast.LENGTH_LONG).show();

                return true;

            case R.id.remove_home:

                if (Common.Database.Bookmark_Remove(iso, province, name, "HOME"))
                    Toast.makeText(getActivity(), getString(R.string.Operation_Success), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.Operation_Failed), Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static ProvinceReportFragment newInstance(String p_iso, Date p_date, String p_province, String p_name) {
        ProvinceReportFragment fragment = new ProvinceReportFragment();

        iso = p_iso;
        selected_date = p_date;
        province = p_province;
        name = p_name;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        tv_region = v.findViewById(R.id.tv_region);
        tv_date = v.findViewById(R.id.tv_date);
        tv_confirmed_cases = v.findViewById(R.id.tv_confirmed_cases);
        tv_confirmed_diff = v.findViewById(R.id.tv_confirmed_diff);
        tv_deaths = v.findViewById(R.id.tv_deaths);
        tv_deaths_diff = v.findViewById(R.id.tv_deaths_diff);
        tv_recovered = v.findViewById(R.id.tv_recovered);
        tv_recovered_diff = v.findViewById(R.id.tv_recovered_diff);
        tv_active = v.findViewById(R.id.tv_active);
        tv_active_diff = v.findViewById(R.id.tv_active_diff);
        tv_fatality_rate = v.findViewById(R.id.tv_fatality_rate);

        if (GlobalVariables.isNetworkConnected)
            ((MainActivity) getActivity()).getReportByProvince(iso, selected_date, province, this);
        else
            Toast.makeText(getActivity(),getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();

        Button btn_refresh = v.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        if (!Common.Database.Get_Configurazione("HIDE_INSTRUCTION").equals("1"))
            Show_Instruction();

        hideKeyboard(getActivity());

        return v;
    }

    public void Show_Instruction()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.Instruction_Title))
                .setMessage(getString(R.string.Instruction_Text))
                .setPositiveButton(getString(R.string.Dont_Show_Anymore), (dialog2, which) -> Common.Database.Set_Configurazione("HIDE_INSTRUCTION","1"))
                .show();
    }

    @Override
    public void newProvinceReportAvailable(Province_Response wResponse) {

        if (wResponse.getDatas().size()==0)
        {
            Toast.makeText(getActivity(),getString(R.string.No_Report_Available), Toast.LENGTH_LONG).show();

            if (province!=null)
                ((MainActivity) getActivity()).goToListprovincesFragment(iso);
            else
                ((MainActivity) getActivity()).goToWorldStats();
        }
        else if (wResponse.getDatas().size()==1)
        {
            tv_date.setText(wResponse.getDatas().get(0).getdate_dd_MM_yyyy());

            tv_confirmed_cases.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getDatas().get(0).getconfirmed())));
            tv_confirmed_diff.setText(String.format("%s +%s", getString(R.string.Title_Confirmed_Cases_Diff), AddDotToInteger(wResponse.getDatas().get(0).getconfirmed_diff())));

            tv_deaths.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getDatas().get(0).getdeaths())));
            tv_deaths_diff.setText(String.format("%s +%s", getString(R.string.Title_deaths_diff), AddDotToInteger(wResponse.getDatas().get(0).getdeaths_diff())));

            if (wResponse.getDatas().get(0).getrecovered()!=0)
            {
                tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getDatas().get(0).getrecovered())));
                tv_recovered_diff.setText(String.format("%s +%s", getString(R.string.Title_recovered_diff), AddDotToInteger(wResponse.getDatas().get(0).getrecovered_diff())));
            }
            else
            {
                tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), getString(R.string.Not_Available)));
                tv_recovered_diff.setText(String.format("%s %s", getString(R.string.Title_recovered_diff), getString(R.string.Not_Available)));
            }

            tv_active.setText(String.format("%s %s", getString(R.string.Title_active), AddDotToInteger(wResponse.getDatas().get(0).getactive())));

            int diff = wResponse.getDatas().get(0).getactive_diff();

            if (diff>0)
                tv_active_diff.setText(String.format("%s +%s", getString(R.string.Title_active_diff), AddDotToInteger(diff)));
            else
                tv_active_diff.setText(String.format("%s %s", getString(R.string.Title_active_diff), AddDotToInteger(diff)));

            DecimalFormat df = new DecimalFormat("#.##");
            tv_fatality_rate.setText(String.format("%s %s%%", getString(R.string.Title_fatality), df.format(wResponse.getDatas().get(0).getfatality_rate()*100)));

            if (wResponse.getDatas().get(0).getregion().province!=null && !wResponse.getDatas().get(0).getregion().province.equals(""))
                tv_region.setText(String.format("%s (%s)", wResponse.getDatas().get(0).getregion().province, wResponse.getDatas().get(0).getregion().name));
            else
                tv_region.setText(wResponse.getDatas().get(0).getregion().name);
        }
        else
        {
            final ProgressDialog waiting_bar = ((MainActivity)getActivity()).getprogressDialog();
            waiting_bar.show();

            tv_date.setText(wResponse.getDatas().get(0).getdate_dd_MM_yyyy());
            tv_region.setText(wResponse.getDatas().get(0).getregion().name);

            int confirmed=0;
            int confirmed_diff=0;

            int deaths=0;
            int deaths_diff=0;

            int recovered=0;
            int recovered_diff=0;

            int active=0;
            int active_diff=0;

            double fatality_rate=0;

            for(Data_Reports var : wResponse.getDatas())
            {
                confirmed += var.getconfirmed();
                confirmed_diff += var.getconfirmed_diff();

                deaths += var.getdeaths();
                deaths_diff += var.getdeaths_diff();

                recovered += var.getrecovered();
                recovered_diff += var.getrecovered_diff();

                active += var.getactive();
                active_diff += var.getactive_diff();

                fatality_rate+=var.getfatality_rate();
            }

            tv_confirmed_cases.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(confirmed)));
            tv_confirmed_diff.setText(String.format("%s +%s", getString(R.string.Title_Confirmed_Cases_Diff), AddDotToInteger(confirmed_diff)));

            tv_deaths.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(deaths)));
            tv_deaths_diff.setText(String.format("%s +%s", getString(R.string.Title_deaths_diff), AddDotToInteger(deaths_diff)));

            if (recovered!=0) {
                tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(recovered)));
                tv_recovered_diff.setText(String.format("%s +%s", getString(R.string.Title_recovered_diff), AddDotToInteger(recovered_diff)));
            }
            else
            {
                tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), getString(R.string.Not_Available)));
                tv_recovered_diff.setText(String.format("%s %s", getString(R.string.Title_recovered_diff), getString(R.string.Not_Available)));
            }

            tv_active.setText(String.format("%s %s", getString(R.string.Title_active), AddDotToInteger(active)));

            if (active_diff>0)
                tv_active_diff.setText(String.format("%s +%s", getString(R.string.Title_active_diff), AddDotToInteger(active_diff)));
            else
                tv_active_diff.setText(String.format("%s %s", getString(R.string.Title_active_diff), AddDotToInteger(active_diff)));

            DecimalFormat df = new DecimalFormat("#.##");
            tv_fatality_rate.setText(String.format("%s %s%%", getString(R.string.Title_fatality), df.format((fatality_rate/wResponse.getDatas().size())*100)));

            waiting_bar.dismiss();
        }

    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {}

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).Update_Province_Report(iso, province, this);
    }
}
