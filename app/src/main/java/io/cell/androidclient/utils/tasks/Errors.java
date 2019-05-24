package io.cell.androidclient.utils.tasks;

public enum Errors {
    LOAD_SERVER_UNAVALABLE("Сервер не доступен."),
    LOAD_CELL_REQUEST_FAILED("Не удалось загрузить область."),
    LOAD_IMAGE_REQUEST_FAILED("Не удалось загрузить изображения.");

    private String description;

    private Errors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
