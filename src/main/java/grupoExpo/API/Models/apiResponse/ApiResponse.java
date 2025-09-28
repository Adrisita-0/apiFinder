package grupoExpo.API.Models.apiResponse;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp;

    // Constructores
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }

    // Métodos estáticos para respuestas comunes
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operación exitosa", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) { return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<?> error(String message) {

        return new ApiResponse<>(false, message, null);
    }

    public ApiResponse<?> error(String message, T data){return new ApiResponse<>(true, message, data);}
}

}
