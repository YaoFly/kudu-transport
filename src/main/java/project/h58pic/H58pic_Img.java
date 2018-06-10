package project.h58pic;

import domain.BaseTable;
import kudu.Key;
import org.apache.kudu.Type;

public class H58pic_Img extends BaseTable{

    @Key(type = Type.INT32)
    private Integer qt_uid;

    @Key(type = Type.STRING)
    private String img_id;

    public Integer getQt_uid() {
        return qt_uid;
    }

    public void setQt_uid(Integer qt_uid) {
        this.qt_uid = qt_uid;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }
}
