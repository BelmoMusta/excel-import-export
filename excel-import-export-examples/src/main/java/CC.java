import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

@Excel(ignoreHeaders = false)
@CSV(useFQNs = true)
public class CC {
	private String h;
	
	//@ToColumn
	@ToColumn(name = "musta") public String getH() {
		return h;
	}
}
