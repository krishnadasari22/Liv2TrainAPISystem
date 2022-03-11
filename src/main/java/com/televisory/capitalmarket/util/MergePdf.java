package com.televisory.capitalmarket.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
 
public class MergePdf {
     public static void main(String[] args)   throws IOException, DocumentException {
        String RESULT= "result.pdf";
    	String[] files = {"report.pdf", "hitesh.pdf"};
    	mergePDF(files,RESULT);
    }
     
    public static boolean mergePDF(String sourcefiles[],String targetfile){
    	Document document = new Document();
        PdfCopy copy;
		try {
			copy = new PdfCopy(document, new FileOutputStream(targetfile));
		    document.open();
	        PdfReader reader;
	        int n;
	        for (int i = 0; i < sourcefiles.length; i++) {
	            reader = new PdfReader(sourcefiles[i]);
	            n = reader.getNumberOfPages();
	            for (int page = 0; page < n; ) 
	                copy.addPage(copy.getImportedPage(reader, ++page));
	            copy.freeReader(reader);
	            reader.close();
	        }
	        document.close();
	        System.out.println("Info :: Merging Complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }

    public static int insertPageNumberPortrait(String filenameinput,int firstpagenumber,int totalindexpages,String filenameoutput)
    {
        PdfStamper stamper;
        int n=-1;
		try {
			PdfReader reader = new PdfReader(filenameinput);
			stamper = new PdfStamper(reader, new FileOutputStream(filenameoutput));
		    n = reader.getNumberOfPages();
		    if(n>totalindexpages)
		        for (int i = totalindexpages+1; i <= n; i++) 
		            getHeaderTable(i+firstpagenumber-1, n).writeSelectedRows(0, -1, 50, 40, stamper.getOverContent(i));
	        stamper.close();
	        reader.close();
		} catch (DocumentException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return n;
    }

    public static int insertPageNumberLandscape(String filenameinput,int firstpagenumber,int totalindexpages,String filenameoutput)
    {
        PdfStamper stamper;
        int n=-1;
		try {
			PdfReader reader = new PdfReader(filenameinput);
			stamper = new PdfStamper(reader, new FileOutputStream(filenameoutput));
		    n = reader.getNumberOfPages();
		    if(n>totalindexpages)
		        for (int i = totalindexpages+1; i <= n; i++) 
		            getHeaderTable(i+firstpagenumber-1, n).writeSelectedRows(0, -1, 305, 45, stamper.getOverContent(i));
	        stamper.close();
	        reader.close();
		} catch (DocumentException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return n;
    }
    
    public static int insertPageNumberLandscapeBasic(String filenameinput,int firstpagenumber,int totalindexpages,String filenameoutput)
    {
        PdfStamper stamper;
        int n=-1;
		try {
			PdfReader reader = new PdfReader(filenameinput);
			stamper = new PdfStamper(reader, new FileOutputStream(filenameoutput));
		    n = reader.getNumberOfPages();
		    if(n>totalindexpages)
		        for (int i = totalindexpages+1; i <= n; i++) 
		            getHeaderTable(i+firstpagenumber-1, n).writeSelectedRows(0, -1, 283, 45, stamper.getOverContent(i));
	        stamper.close();
	        reader.close();
		} catch (DocumentException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return n;
    }

    public static PdfPTable getHeaderTable(int x, int y) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(500);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        Font font = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        //Phrase p = new Phrase(String.format("%d of %d", x, y), font);
        Phrase p = new Phrase(String.format("Page | %d",x), font);
        table.addCell(p);
        return table;
    }
    
}