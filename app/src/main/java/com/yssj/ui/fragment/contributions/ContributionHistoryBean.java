package com.yssj.ui.fragment.contributions;

import java.io.Serializable;
import java.util.List;

public class ContributionHistoryBean implements Serializable {

    private String message;
    private String status;
    private List<DataDTO> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        private int id;
        private int status;
        private String shop_specification;
        private String shop_size;
        private int creator;
        private long create_date;
        private List<?> supplyMaterialImageEntitys;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getShop_specification() {
            return shop_specification;
        }

        public void setShop_specification(String shop_specification) {
            this.shop_specification = shop_specification;
        }

        public String getShop_size() {
            return shop_size;
        }

        public void setShop_size(String shop_size) {
            this.shop_size = shop_size;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public List<?> getSupplyMaterialImageEntitys() {
            return supplyMaterialImageEntitys;
        }

        public void setSupplyMaterialImageEntitys(List<?> supplyMaterialImageEntitys) {
            this.supplyMaterialImageEntitys = supplyMaterialImageEntitys;
        }
    }
}
