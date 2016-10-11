package raiload.statistics;

import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

import static raiload.statistics.StatisticsEJB.getStatistics;

/**
 * JSF Bean
 * for "statistics.xhtml" page
 *
 * @author vbuevich
 */
@ManagedBean
@SessionScoped
public class StatisticsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date startTime; // start period of statistics
    private Date endTime; // end period of statistics
	private StreamedContent pdfDocument; // streamed content of PDF document which will be generated from statistics received from REST service

	/**
	 * Method to handle button click event when user wants to get statistics
	 *
	 * @return refreshes "statistics.xhtml" page
	 */
    public String statistics() {

		Long sT = this.getStartTime().getTime(); // milliseconds from startTime
		String startTime = sT.toString(); // the String of milliseconds so we can pass them as parameter to REST service
		Long eT = this.getEndTime().getTime(); // milliseconds from endTime
		String endTime = eT.toString(); // the String of milliseconds so we can pass them as parameter to REST service
		ArrayList<Statistics> statisticsList = getStatistics(startTime, endTime); // getting List of Statistics from REST service via EJB

		StreamedContent document = PdfStatisticsView.buildPdfDocument(statisticsList); // creating PDF
		this.setPdfDocument(document); // setting PDF as field in bean

        return "statistics"; // reloading page to get updated PDF view
    }

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public StreamedContent getPdfDocument() {
		return pdfDocument;
	}

	public void setPdfDocument(StreamedContent pdfDocument) {
		this.pdfDocument = pdfDocument;
	}
}