package raiload.statistics;

import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

import static raiload.statistics.StatisticsEJB.getStatistics;

@ManagedBean
@SessionScoped
public class StatisticsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date startTime;
    private Date endTime;
	private StreamedContent pdfDocument;

    public String statistics() {
        // Getting statistics here
		Long sT = this.getStartTime().getTime();
		String startTime = sT.toString();
		Long eT = this.getEndTime().getTime();
		String endTime = eT.toString();
		ArrayList<Statistics> statisticsList = getStatistics(startTime, endTime);

		StreamedContent document = PdfStatisticsView.buildPdfDocument(statisticsList);
		this.setPdfDocument(document);

        return "index";
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