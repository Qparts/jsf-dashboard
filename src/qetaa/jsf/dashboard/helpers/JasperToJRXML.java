package qetaa.jsf.dashboard.helpers;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

public class JasperToJRXML {

	 public static String sourcePath,destinationPath,xml;


	 public static JasperDesign jd = new JasperDesign();

	 public static void compile() throws JRException{
		 String dir = "C:\\Users\\faree\\eclipse-workspace\\app_jsf_qetaa_dashboard\\WebContent\\WEB-INF\\jasper\\";
		 File file = new File(dir + "quotation.jrxml");
		 System.out.println(file.getAbsolutePath());
		 JasperCompileManager.compileReportToFile(file.getAbsolutePath(), dir + "quotation.jasper");
	 }
	 
	 public static void decompile() throws JRException {
		 // Paths
		 File dir = new File("C:\\Users\\faree\\eclipse-workspace\\app_jsf_qetaa_dashboard\\WebContent\\WEB-INF\\jasper\\");
		 if (dir.isDirectory()) {
			 File[] jaspers = dir.listFiles();
			 for (int i = 0; i < jaspers.length; i++) {
				 	String outputPath = "C:\\Users\\faree\\eclipse-workspace\\app_jsf_qetaa_dashboard\\WebContent\\WEB-INF\\jasper\\";
				 	String ext = getFileExtenstion(jaspers[i]);
				 	if (ext.equalsIgnoreCase(".jasper")) {
				 		JasperReport report = (JasperReport)JRLoader.loadObject(jaspers[i]);
				 		outputPath += getFileName(jaspers[i]) + ".jrxml";
				 		JRXmlWriter.writeReport(report, outputPath, "UTF-8");

				 	}
				 	System.out.println(ext);
			 }
		 }

	 }

	 private static String getFileExtenstion(File file) {
		 String fileName = file.getName();
		 int i = fileName.indexOf(".");
		 return fileName.substring(i, fileName.length());
	 }

	 private static String getFileName(File file) {
		 String fileName = file.getName();
		 int i = fileName.indexOf(".");
		 return fileName.substring(0, i);
	 }
}
