package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class DataTableRequestDTO<T> {

    private static final String ASC = "asc";

    private static final String DEFAULT_SORT_COLUMN = "Id";

    private static final String DEFAULT_AGGREGATION_SORT_COLUMN = "id";

    private String sortColumn;

    private String sortOrder;

    private Integer pageSize;

    private Integer pageIndex;

    private Boolean fetchAllRecords = Boolean.FALSE;

    private String searchText;

    private T query;

    /**
     * Gets sort column.
     * @return the sort column
     */
    public String getSortColumn() {
        if (StringUtils.isEmpty(this.sortColumn)) {
            this.sortColumn = DEFAULT_SORT_COLUMN;
        }
        return this.sortColumn;
    }

    /**
     * Gets sort column when aggregation is used.
     * @return the sort column
     */
    public String getAggregationSortColumn() {
        if (StringUtils.isEmpty(this.sortColumn)) {
            this.sortColumn = DEFAULT_AGGREGATION_SORT_COLUMN;
        }
        return this.sortColumn;
    }

    /**
     * Gets order criteria.
     * @return the order criteria
     */
    @JsonIgnore
    public Sort.Direction getSortDirection() {
        if (StringUtils.isEmpty(this.sortOrder)) {
            this.sortOrder = ASC;
        }
        return Sort.Direction.fromString(sortOrder);
    }

    /**
     * Gets page size.
     * @return the page size
     */
    public Integer getPageSize() {
        if ((pageSize == null) || (pageSize <= 0)) {
            return 10;
        }
        return pageSize;
    }

    /**
     * Gets page index.
     * @return the page index
     */
    public Integer getPageIndex() {
        if ((pageIndex == null) || (pageIndex < 0)) {
            return 0;
        }
        return pageIndex;
    }

    /**
     * Gets max.
     * @return the max
     */
    @JsonIgnore
    public Integer getMax() {
        return getPageSize();
    }

    /**
     * Gets offset.
     * @return the offset
     */
    public Integer getOffset() {
        return getPageIndex() * getPageSize();
    }
}
