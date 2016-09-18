package com.github.bubinimara.multilanguage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davide on 4/08/16.
 */
public class ProductModel extends AbModel implements Parcelable {
    private int categoryId;

    private String name;
    private String description;
    private String image;

    private int order;

    public ProductModel() {
    }

    /**
     * @param productId the id
     * @param categoryId the category id
     * @param name the name
     * @param description the description
     * @param image the image path
     * @param order the order
     */
    public ProductModel(int productId,int categoryId, String name, String description, String image, int order) {
        super.id = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.order = order;
    }

    protected ProductModel(Parcel in) {
        categoryId = in.readInt();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        order = in.readInt();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeInt(order);
    }
}
