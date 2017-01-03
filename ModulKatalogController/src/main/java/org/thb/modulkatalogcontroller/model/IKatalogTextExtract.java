package org.thb.modulkatalogcontroller.model;

import java.io.IOException;
import java.util.Map;

/**
 * Every Class which implements the inteface provides the service of extracting the text of a given Catalog. The controlsMap controls
 * how the extraction is done.
 * @author Manuel Raddatz
 *
 */
public interface IKatalogTextExtract
{

	public String extractKatalogText(byte[] b, Map<String, Integer> controls)throws IOException;
}
