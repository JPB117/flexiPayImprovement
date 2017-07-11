package com.workpoint.icpak.tests.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestOutput extends AbstractDaoTest {

	@Inject
	CPDDaoHelper helper;

	@Test
	public void print() throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {
		CPDDto cpd = helper.getCPD("dbd043Iq5LYQ8pd3");

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventName", cpd.getTitle());
		values.put("eventDates",
				DateUtils.formatDate(cpd.getStartDate(), "dd/MM/yyyy") + " to "
						+ DateUtils.formatDate(cpd.getEndDate(), "dd/MM/yyyy"));
		values.put("memberName", cpd.getFullNames());
		values.put("dateIssued", DateUtils.formatDate(new Date(), "dd/MM/yyyy"));
		values.put("cpdHours",
				NumberUtils.NUMBERFORMAT.format(cpd.getCpdHours()));
		Doc doc = new Doc(values);

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = TestOutput.class.getClassLoader().getResourceAsStream(
				"cpdcertificate.html");
		String html = IOUtils.toString(is);
		byte[] bite = convertor.convert(doc, html);

		IOUtils.copy(new ByteArrayInputStream(bite), new FileOutputStream(
				new File("cpdcert.pdf")));
	}
}
