package project.h58pic;

import domain.BaseTable;
import kudu.ImpalaColumn;
import kudu.Key;
import org.apache.kudu.Type;

public class H58pic extends BaseTable{

    @ImpalaColumn(type = Type.STRING)
    private String remote_addr;

    @ImpalaColumn(type = Type.STRING)
    private String remote_user;

    @ImpalaColumn(type = Type.INT64)
    private Long time_local;

    @ImpalaColumn(type = Type.STRING)
    private String request_method;

    @ImpalaColumn(type = Type.STRING)
    private String request_path;

    @ImpalaColumn(type = Type.STRING)
    private String request_version;

    @ImpalaColumn(type = Type.INT16)
    private Short status;

    @ImpalaColumn(type = Type.INT32)
    private Integer body_bytes_sent;

    @ImpalaColumn(type = Type.STRING)
    private String http_referer;

    @ImpalaColumn(type = Type.STRING)
    private String http_user_agent;

    @ImpalaColumn(type = Type.STRING)
    private String http_x_forwarded_for;

    @ImpalaColumn(type = Type.STRING)
    private String upstream_cache_status;

    @ImpalaColumn(type = Type.DOUBLE)
    private Double upstream_response_time;

    @ImpalaColumn(type = Type.DOUBLE)
    private Double request_time;

    @ImpalaColumn(type = Type.INT32)
    private Integer qt_createtime;

    @ImpalaColumn(type = Type.STRING)
    private String qt_type;

    @Key(type = Type.INT32)
    private Integer qt_uid;

    @ImpalaColumn(type = Type.INT32)
    private Integer qt_utime;

    @Key(type = Type.STRING)
    private String qt_visitor_id;

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public String getRequest_method() {
        return request_method;
    }

    public void setRequest_method(String request_method) {
        this.request_method = request_method;
    }

    public String getRequest_path() {
        return request_path;
    }

    public void setRequest_path(String request_path) {
        this.request_path = request_path;
    }

    public String getRequest_version() {
        return request_version;
    }

    public void setRequest_version(String request_version) {
        this.request_version = request_version;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getHttp_x_forwarded_for() {
        return http_x_forwarded_for;
    }

    public void setHttp_x_forwarded_for(String http_x_forwarded_for) {
        this.http_x_forwarded_for = http_x_forwarded_for;
    }

    public String getUpstream_cache_status() {
        return upstream_cache_status;
    }

    public void setUpstream_cache_status(String upstream_cache_status) {
        this.upstream_cache_status = upstream_cache_status;
    }

    public Long getTime_local() {
        return time_local;
    }

    public void setTime_local(Long time_local) {
        this.time_local = time_local;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(Integer body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public Double getUpstream_response_time() {
        return upstream_response_time;
    }

    public void setUpstream_response_time(Double upstream_response_time) {
        this.upstream_response_time = upstream_response_time;
    }

    public Double getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Double request_time) {
        this.request_time = request_time;
    }

    public Integer getQt_createtime() {
        return qt_createtime;
    }

    public void setQt_createtime(Integer qt_createtime) {
        this.qt_createtime = qt_createtime;
    }

    public String getQt_type() {
        return qt_type;
    }

    public void setQt_type(String qt_type) {
        this.qt_type = qt_type;
    }

    public Integer getQt_uid() {
        return qt_uid;
    }

    public void setQt_uid(Integer qt_uid) {
        this.qt_uid = qt_uid;
    }

    public Integer getQt_utime() {
        return qt_utime;
    }

    public void setQt_utime(Integer qt_utime) {
        this.qt_utime = qt_utime;
    }

    public String getQt_visitor_id() {
        return qt_visitor_id;
    }

    public void setQt_visitor_id(String qt_visitor_id) {
        this.qt_visitor_id = qt_visitor_id;
    }
}
