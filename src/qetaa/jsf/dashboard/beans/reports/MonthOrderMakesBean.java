package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.component.datatable.DataTable;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.cart.report.MakeOrderMonth;

@Named
@ViewScoped
public class MonthOrderMakesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<MakeOrderMonth> moms;
	private DataTable myDataTable;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		initMoms();
		initDataTable();
	}

	private void initMoms() {
		moms = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_MAKE_ORER_MONTHS);
		if (r.getStatus() == 200) {
			moms = r.readEntity(new GenericType<List<MakeOrderMonth>>() {
			});
		}
	}
	
	private void initDataTable() {
		
	}

	public List<String> getPast12Months() {
		Calendar calendar = Calendar.getInstance();
		List<Calendar> allDates = new ArrayList<>();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 0; i <= 11; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(calendar.getTime());
			c.add(Calendar.MONTH, -i);
			allDates.add(c);
		}
		
		Collections.sort(allDates, new Comparator<Calendar>() {
			@Override
			public int compare(Calendar o1, Calendar o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		List<String> names = new ArrayList<>();
		for(Calendar c : allDates) {
			names.add(getMonthForInt(c.get(Calendar.MONTH)));
		}
		return names;

	}

	String getMonthForInt(int num) {
		String month = "";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	public List<MakeOrderMonth> getMoms() {
		return moms;
	}

	public void setMoms(List<MakeOrderMonth> moms) {
		this.moms = moms;
	}

	public DataTable getMyDataTable() {
		return myDataTable;
	}

	public void setMyDataTable(DataTable myDataTable) {
		this.myDataTable = myDataTable;
	}
	
	

}
