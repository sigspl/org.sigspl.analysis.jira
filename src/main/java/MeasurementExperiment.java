import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.sigspl.analysis.commons.AssetStorage;

import org.sigspl.analysis.commons.MeasurementSeries;
import org.sigspl.analysis.commons.PlainMetricProtocol;

import org.sigspl.analysis.commons.ifaces.IMeasurement;
import org.sigspl.analysis.commons.labeling.SimpleLabeler;
import org.sigspl.analysis.commons.measurements.documentation.MeasureVocabularyUsage;
import org.sigspl.analysis.commons.measurements.documentation.MeasureWritingStyle;
import org.sigspl.analysis.commons.util.Constants;
import org.sigspl.analysis.commons.util.MathUtil;
import org.sigspl.analysis.commons.util.StringUtil;
import org.sigspl.analysis.commons.util.ValueComparator;
import org.sigspl.analysis.jira.models.Issue;

public class MeasurementExperiment  {

	

	private AssetStorage assets;
	private HashMap<String, Double> measurementResults = new HashMap<String, Double>();
	private PlainMetricProtocol protocol = new PlainMetricProtocol();

	public void setStorage(AssetStorage d) {
		assets = d;
	}


	public void executeAll() {
		String out = "";
		for (Iterator<String> iterator = assets.getAssets().keySet()
				.iterator(); iterator.hasNext();) {
			String n = (String) iterator.next();
			Issue issue = (Issue)assets.getAssets().get((n));

			MeasurementSeries c = new MeasurementSeries();
			LinkedList<IMeasurement> measurements = new LinkedList<IMeasurement>();
			measurements.add(new MeasureVocabularyUsage());
			measurements.add(new MeasureWritingStyle());
			c.setMeasurements(measurements);
			c.setMetricsOutputFile(Configuration.METRICS_TRANSCRIPT_FILENAME);
			
			c.setAsset(issue);
			c.setLogCollector(protocol);
			double score = c.applyMeasurements(); 
			
			measurementResults.put(issue.getCompoundIdentificator(),
					MathUtil.round(score, Constants.DECIMAL_PRECISION));

		}

		// sort collected values
		ValueComparator bvc = new ValueComparator(measurementResults);
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(
				bvc);
		sorted_map.putAll(measurementResults);

		SimpleLabeler sl = new SimpleLabeler();
		
		String protocolTranscript="Date of measurement procedure: " + StringUtil.getTodayIsoDate() + "\n";
		protocolTranscript+="Used data scraped as of: " + StringUtil.getFileIsoDateLastModified(Configuration.DATA_SOURCE)+"\n\n\n\n";
		
		
		for (Map.Entry<String, Double> sample : sorted_map.entrySet()) {
			String key = sample.getKey();
			Double value = sample.getValue();

			// output of labels as CSV table
			String entry = StringUtil.dquote(key) + StringUtil.CSV_SEPARATOR + StringUtil.dquote(MathUtil.formatDouble(value))
					+ StringUtil.CSV_SEPARATOR + StringUtil.dquote( sl.produceLinearLabel(value));
			out = out + entry + "\n";

			protocolTranscript += "\n++++++++\n\n";
			//System.out.println("!!! " + " KEY  " + key);
			protocolTranscript += (protocol.getLog(assets.find(key)));
			/**
			 * HTMLReport html = new HTMLReport(); html.setProtocol(protocol);
			 * html.generate();
			 */

			//System.out.println(protocolTranscript);

		} // end for
		try {
			FileUtils.writeStringToFile(
					new File(Configuration.MEASUREMENT_TRANSCRIPT_TXT),
					protocolTranscript);
		} catch (IOException e) {
			// TODO add log entry
			e.printStackTrace();
		}

		System.out.println(out);

		// write a file; TODO: make a configuration option or implement further
		// data flow
		try {
			FileUtils.writeStringToFile(
					new File(Configuration.MEASUREMENT_DATA_FILENAME), out);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	
	
}
