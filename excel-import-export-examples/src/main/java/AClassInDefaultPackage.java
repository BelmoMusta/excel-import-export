import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.common.ExportType;

@Export(type = ExportType.EXCEL, ignoreHeaders = false)
public class AClassInDefaultPackage {
    private String h;
    
    @ToColumn(name = "musta")
    public String getH() {
        return h;
    }
}
