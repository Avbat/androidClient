package io.cell.androidclient.utils.tasks;

public enum Errors {
    LOAD_SERVER_UNAVALABLE("Сервер не доступен."),
    LOAD_CELL_REQUEST_FAILED("Не удалось загрузить область."),
    LOAD_IMAGE_REQUEST_FAILED("Не удалось загрузить изображения."),

    LOAD_TRY_AGAIN("Повторите попытку позже.");

    private String description;

    Errors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
