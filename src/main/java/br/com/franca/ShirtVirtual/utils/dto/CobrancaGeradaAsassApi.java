package br.com.franca.ShirtVirtual.utils.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal para gerar a cobrança no Asaas
 * <p>
 *     <br>
 * @Author França
 * Created by França on 25/09/2018.
 */

public class CobrancaGeradaAsassApi {

    private String object;
    private Boolean hasMore;
    private Integer totalCount;
    private Integer limit;
    private Integer offset;

    private List<CobrancaGeradaAssasData> data = new ArrayList<CobrancaGeradaAssasData>();

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<CobrancaGeradaAssasData> getData() {
        return data;
    }

    public void setData(List<CobrancaGeradaAssasData> data) {
        this.data = data;
    }

}