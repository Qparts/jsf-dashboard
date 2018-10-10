package qetaa.jsf.dashboard.beans.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.product.PriceHolder;

@Named
@ViewScoped
public class PriceListUploadBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String BASE = "C:\\Users\\faree\\Desktop\\PriceList\\Kia\\";
	private static final String FILE_NAME = BASE + "kia.xlsx";
	private List<PriceHolder> priceHolders;
	private List<String> errors;
	private int vendorId;
	private int makeId;

	@PostConstruct
	private void init() {
		priceHolders = new ArrayList<>();
	}

	public void readExcel() {
		try {
			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			iterator.next();// skip first row
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				PriceHolder holder = new PriceHolder();
				// Iterator<Cell> cellIterator = currentRow.iterator();
				if (currentRow.getCell(0).getCellTypeEnum() == CellType.STRING) {
					holder.setNumber(currentRow.getCell(0).getStringCellValue().trim().toUpperCase().substring(2));
				} else {
					this.errors.add("Number cell is not string");
				}

				if (currentRow.getCell(1).getCellTypeEnum() == CellType.STRING) {
					holder.setDesc(currentRow.getCell(1).getStringCellValue());
				} else {
					this.errors.add("Name cell is not string");
				}

				if (currentRow.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
					holder.setPriceWv(currentRow.getCell(2).getNumericCellValue());
				} else {
					this.errors.add("Price cell is not double");
				}
				holder.setPrice(Helper.deductAddedPercentage(holder.getPriceWv(), .05));
				holder.setMakeId(makeId);
				holder.setVendorId(vendorId);
				this.priceHolders.add(holder);
				// while (cellIterator.hasNext()) {
				// Cell currentCell = cellIterator.next();
				// getCellTypeEnum shown as deprecated for version 3.15
				// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
				// if (currentCell.getCellTypeEnum() == CellType.STRING) {
				// String partName = currentCell.getStringCellValue();
				// } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
				// }

				// }
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<PriceHolder> getPriceHolders() {
		return priceHolders;
	}

	public void setPriceHolders(List<PriceHolder> priceHolders) {
		this.priceHolders = priceHolders;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

}
