package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtility {

	public static XMLGregorianCalendar now() {
		LocalDateTime dateTime = LocalDateTime.now();
		String iso = dateTime.toString();
		iso += (dateTime.getSecond() == 0 && dateTime.getNano() == 0) ? ":00" : "";
		return toGregorianCalendar(iso);
	}
	
	public static XMLGregorianCalendar toGregorianCalendar(String dateTime) {
		try {
			if (dateTime == null) return null;
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTime);
		} catch (DatatypeConfigurationException e) {
			log.error("Error mapping %s dateTime [String] to XmlGregorianCalendar", dateTime);
			return null;
		}
	}
	
	public static XMLGregorianCalendar toGregorianCalendar(Date date) {
		try {
			if (date == null) return null;
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			log.error("Error mapping %s date [Date] to XmlGregorianCalendar", date);
			return null;
		}
	}
	
	public static Date getEndOfDay(Date date) {
		 return new DateTime(date)
				 .withTimeAtStartOfDay()
				 .plusDays(1)
				 .minusSeconds(1)
				 .toDate();
	}
	
	public static Date getStartOfDay(Date date) {
		 return new DateTime(date)
				 .withTimeAtStartOfDay()
				 .toDate();
	}

	public static Date toDate(XMLGregorianCalendar date) {
		if (date == null) return null;
		return date.toGregorianCalendar().getTime();
	}

}
