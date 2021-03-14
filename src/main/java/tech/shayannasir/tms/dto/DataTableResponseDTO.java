package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTableResponseDTO<Q, T> {

    private static final String ASC = "asc";

    private static final String DEFAULT_SORT_COLUMN = "Id";
    private long recordsTotal;
    private long recordsFiltered;
    private String sortColumn;
    private String sortOrder;
    private Integer pageSize;
    private Integer pageIndex;
    private Boolean fetchAllRecords = Boolean.FALSE;
    private Boolean status = true;
    private String message;
    private String exportFileName;
    private T data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ErrorDTO> errors = new ArrayList<>();
    private Q query;

    /**
     * Gets instance.
     *
     * @param <Q>                 the type parameter
     * @param <T>                 the type parameter
     * @param data                the data
     * @param recordsFiltered     the records filtered
     * @param dataTableRequestDTO the data table request dto
     * @return the instance
     */
    public static <Q, T> DataTableResponseDTO<Q, List<T>> getInstance(List<T> data,
                                                                      long recordsFiltered,
                                                                      DataTableRequestDTO<Q> dataTableRequestDTO) {
        DataTableResponseDTO<Q, List<T>> dataTableResponseDTO = new DataTableResponseDTO<>();
        dataTableResponseDTO.setPageIndex(dataTableRequestDTO.getPageIndex());
        dataTableResponseDTO.setPageSize(dataTableRequestDTO.getPageSize());
        dataTableResponseDTO.setSortColumn(dataTableRequestDTO.getSortColumn());
        dataTableResponseDTO.setSortOrder(dataTableRequestDTO.getSortOrder());
        dataTableResponseDTO.setQuery(dataTableRequestDTO.getQuery());
        return setInstance(data, recordsFiltered, dataTableResponseDTO);
    }

    /**
     * Gets instance.
     *
     * @param <Q>             the type parameter
     * @param <T>             the type parameter
     * @param data            the data
     * @param recordsFiltered the records filtered
     * @return the instance
     */
    public static <Q, T> DataTableResponseDTO<Q, List<T>> getInstance(List<T> data, long recordsFiltered) {
        DataTableResponseDTO<Q, List<T>> dataTableResponseDTO = new DataTableResponseDTO<>();
        return setInstance(data, recordsFiltered, dataTableResponseDTO);
    }

    /**
     * Sets instance.
     *
     * @param <Q>                  the type parameter
     * @param <T>                  the type parameter
     * @param data                 the data
     * @param recordsFiltered      the records filtered
     * @param dataTableResponseDTO the data table response dto
     * @return the instance
     */
    private static <Q, T> DataTableResponseDTO<Q, List<T>> setInstance(List<T> data,
                                                                       long recordsFiltered,
                                                                       DataTableResponseDTO<Q, List<T>>
                                                                               dataTableResponseDTO) {
        if (dataTableResponseDTO == null) {
            dataTableResponseDTO = new DataTableResponseDTO<>();
        }
        dataTableResponseDTO.setData(data);
        dataTableResponseDTO.setRecordsFiltered(recordsFiltered);
        if (CollectionUtils.isEmpty(data)) {
            dataTableResponseDTO.setStatus(false);
            dataTableResponseDTO.setMessage("No record found");
        } else {
            dataTableResponseDTO.setStatus(true);
            dataTableResponseDTO.setMessage("Request processed successfully");
        }
        return dataTableResponseDTO;
    }

    /**
     * Gets instance.
     *
     * @param <Q>     the type parameter
     * @param <T>     the type parameter
     * @param status  the status
     * @param message the message
     * @return the instance
     */
    public static <Q, T> DataTableResponseDTO<Q, List<T>> getInstance(Boolean status, String message) {
        DataTableResponseDTO<Q, List<T>> dataTableResponseDTO = new DataTableResponseDTO<>();
        dataTableResponseDTO.setStatus(status);
        dataTableResponseDTO.setMessage(message);
        return dataTableResponseDTO;
    }

    /**
     * Get instance with query
     *
     * @param status  status
     * @param message message
     * @param query   query
     * @param <Q>     the type parameter
     * @param <T>     the type parameter
     * @return the instance
     */
    public static <Q, T> DataTableResponseDTO<Q, List<T>> getInstance(Boolean status, String message, Q query) {
        DataTableResponseDTO<Q, List<T>> dataTableResponseDTO = new DataTableResponseDTO<>();
        dataTableResponseDTO.setStatus(status);
        dataTableResponseDTO.setMessage(message);
        dataTableResponseDTO.setQuery(query);
        return dataTableResponseDTO;
    }

    /**
     * @param errorDTO
     */
    public void addToErrors(ErrorDTO errorDTO) {
        this.getErrors().add(errorDTO);
    }
}
