package bet.lucky.game.external_dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = -1123952196162881244L;
    private final String error;
    private final String errorDescription;

    public ErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return error.equals(that.error) && errorDescription.equals(that.errorDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errorDescription);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("error", this.getError());
        errorMap.put("error_description", this.getErrorDescription());
        return errorMap;
    }
}
