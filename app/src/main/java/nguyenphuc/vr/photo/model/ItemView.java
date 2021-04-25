package nguyenphuc.vr.photo.model;

public abstract class ItemView {
    Type type;

    public ItemView(Type type) {
        this.type = type;
    }

    public abstract void setType(Type type);

    public abstract Type getType();
}
