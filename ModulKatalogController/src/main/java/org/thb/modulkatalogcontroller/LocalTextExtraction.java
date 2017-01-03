package org.thb.modulkatalogcontroller;

import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.thb.modulkatalogcontroller.model.IKatalogTextExtract;

/**
 * The LocalTextextration is used in the local Application Server. For AWS use it extraction is done by a lambda function
 * @author Manuel Raddatz 
 *
 */
public class LocalTextExtraction implements IKatalogTextExtract
{
	
	@Override
	public String extractKatalogText(byte[] b, Map<String, Integer> controls) throws IOException
	{
		
		PDDocument pdf = null;
		pdf = PDDocument.load(b);

		PDFTextStripper stripper = null;
		stripper = new PDFTextStripper();
		stripper.setLineSeparator(System.lineSeparator());
		stripper.setSortByPosition(true);
		
		stripper.setStartPage(controls.get("Startseite"));
		stripper.setEndPage(controls.get("Endseite"));
		
		String result = stripper.getText(pdf);
		pdf.close();
		return result;
	}
}
