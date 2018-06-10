package domain;

import kudu.Key;
import org.apache.kudu.Type;

public class BaseTable {
    @Key(type = Type.INT32)
    private Integer line_number;

    @Key(type = Type.INT32,rangeKey = true)
    private Integer date_time;

    public Integer getLine_number() {
        return line_number;
    }

    public void setLine_number(Integer line_number) {
        this.line_number = line_number;
    }

    public Integer getDate_time() {
        return date_time;
    }

    public void setDate_time(Integer date_time) {
        this.date_time = date_time;
    }
}
