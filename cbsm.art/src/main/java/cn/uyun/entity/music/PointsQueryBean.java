package cn.uyun.entity.music;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @ClassName: PointsQueryBean
 * @Description: TODO(性能指标查询参数Bean，在需要查询性能指标数据，调用resful接口时，使用的性能指标参数结构Bean)
 * @date 2017年5月9日 下午3:42:56
 */
public class PointsQueryBean {
    /**
     * 指标编码 必须
     */
    private String metric;
    private boolean is_use_cached = false;
    private Map<String, Object> time;
    private Map<String, Object> tags = new HashMap<>();
    private Map<String, Object> group_by = new HashMap<>();
    private boolean useCache = false;

    public PointsQueryBean() {
    }

    public PointsQueryBean(String metric, Map<String, Object> time, Map<String, Object> tags,
                           Map<String, Object> group_by) {
        this.metric = metric;
        this.time = time;
        this.tags = tags;
        this.group_by = group_by;
    }

    public String getMetric() {
        return metric;
    }

    public boolean getIs_use_cached() {
        return is_use_cached;
    }

    public void setIs_use_cached(boolean is_use_cached) {
        this.is_use_cached = is_use_cached;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Map<String, Object> getTime() {
        return time;
    }

    public void setTime(Map<String, Object> time) {
        this.time = time;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getGroup_by() {
        return group_by;
    }

    public void setGroup_by(Map<String, Object> group_by) {
        this.group_by = group_by;
    }

    @Override
    public String toString() {
        return "PointsQueryBean {\"metric\":" + metric + ",\"is_use_cached:\":"+is_use_cached+", \"time\":" + time + ", \"tags\":" + tags + ", \"group_by\":" + group_by
                + "}";
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

}
