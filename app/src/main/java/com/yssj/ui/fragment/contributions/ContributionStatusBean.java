package com.yssj.ui.fragment.contributions;

import java.io.Serializable;
import java.util.List;

public class ContributionStatusBean implements Serializable {

    private DataDTO data;
    private SupplyMaterialExpressDTO SupplyMaterialExpress;
    private String message;
    private String status;
    private String flow;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public SupplyMaterialExpressDTO getSupplyMaterialExpress() {
        return SupplyMaterialExpress;
    }

    public void setSupplyMaterialExpress(SupplyMaterialExpressDTO supplyMaterialExpress) {
        SupplyMaterialExpress = supplyMaterialExpress;
    }

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

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public static class DataDTO implements Serializable {
        private int id;
        private int status;
        private String shop_specification;
        private String shop_size;
        private int creator;
        private long create_date;
        private long update_date;
        private List<SupplyMaterialImageEntitysDTO> supplyMaterialImageEntitys;

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

        public long getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(long update_date) {
            this.update_date = update_date;
        }

        public List<SupplyMaterialImageEntitysDTO> getSupplyMaterialImageEntitys() {
            return supplyMaterialImageEntitys;
        }

        public void setSupplyMaterialImageEntitys(List<SupplyMaterialImageEntitysDTO> supplyMaterialImageEntitys) {
            this.supplyMaterialImageEntitys = supplyMaterialImageEntitys;
        }

        public static class SupplyMaterialImageEntitysDTO implements Serializable {
            private int id;
            private String real_path;
            private int material_type;
            private int creator;
            private int supply_material_id;
            private long create_date;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReal_path() {
                return real_path;
            }

            public void setReal_path(String real_path) {
                this.real_path = real_path;
            }

            public int getMaterial_type() {
                return material_type;
            }

            public void setMaterial_type(int material_type) {
                this.material_type = material_type;
            }

            public int getCreator() {
                return creator;
            }

            public void setCreator(int creator) {
                this.creator = creator;
            }

            public int getSupply_material_id() {
                return supply_material_id;
            }

            public void setSupply_material_id(int supply_material_id) {
                this.supply_material_id = supply_material_id;
            }

            public long getCreate_date() {
                return create_date;
            }

            public void setCreate_date(long create_date) {
                this.create_date = create_date;
            }
        }
    }

    public static class SupplyMaterialExpressDTO implements Serializable {
        private int id;
        private int creator;
        private String express_company;
        private String express_num;
        private int supply_material_id;
        private long create_date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public String getExpress_company() {
            return express_company;
        }

        public void setExpress_company(String express_company) {
            this.express_company = express_company;
        }

        public String getExpress_num() {
            return express_num;
        }

        public void setExpress_num(String express_num) {
            this.express_num = express_num;
        }

        public int getSupply_material_id() {
            return supply_material_id;
        }

        public void setSupply_material_id(int supply_material_id) {
            this.supply_material_id = supply_material_id;
        }

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }
    }

}
