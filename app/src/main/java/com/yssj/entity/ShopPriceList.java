package com.yssj.entity;

import java.io.Serializable;
import java.util.List;


public class ShopPriceList implements Serializable {

    private String status;
    private List<ShopPrice> price_list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShopPrice> getPrice_list() {
        return price_list;
    }

    public void setPrice_list(List<ShopPrice> price_list) {
        this.price_list = price_list;
    }

//    public static class PriceListDTO implements Serializable {
//        private String type_name;
//        private int parent_id;
//        private int id;
//        private String create_date;
//        private int supply_material_price_list_id;
//        private List<ChildrenDTO> children;
//
//        public String getType_name() {
//            return type_name;
//        }
//
//        public void setType_name(String type_name) {
//            this.type_name = type_name;
//        }
//
//        public int getParent_id() {
//            return parent_id;
//        }
//
//        public void setParent_id(int parent_id) {
//            this.parent_id = parent_id;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getCreate_date() {
//            return create_date;
//        }
//
//        public void setCreate_date(String create_date) {
//            this.create_date = create_date;
//        }
//
//        public int getSupply_material_price_list_id() {
//            return supply_material_price_list_id;
//        }
//
//        public void setSupply_material_price_list_id(int supply_material_price_list_id) {
//            this.supply_material_price_list_id = supply_material_price_list_id;
//        }
//
//        public List<ChildrenDTO> getChildren() {
//            return children;
//        }
//
//        public void setChildren(List<ChildrenDTO> children) {
//            this.children = children;
//        }
//
//        public static class ChildrenDTO implements Serializable {
//            private String type_name;
//            private int parent_id;
//            private int price;
//            private int type_use;
//            private int id;
//            private String create_date;
//            private String type_value;
//            private int supply_material_price_list_id;
//            private List<?> children;
//
//            public String getType_name() {
//                return type_name;
//            }
//
//            public void setType_name(String type_name) {
//                this.type_name = type_name;
//            }
//
//            public int getParent_id() {
//                return parent_id;
//            }
//
//            public void setParent_id(int parent_id) {
//                this.parent_id = parent_id;
//            }
//
//            public int getPrice() {
//                return price;
//            }
//
//            public void setPrice(int price) {
//                this.price = price;
//            }
//
//            public int getType_use() {
//                return type_use;
//            }
//
//            public void setType_use(int type_use) {
//                this.type_use = type_use;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getCreate_date() {
//                return create_date;
//            }
//
//            public void setCreate_date(String create_date) {
//                this.create_date = create_date;
//            }
//
//            public String getType_value() {
//                return type_value;
//            }
//
//            public void setType_value(String type_value) {
//                this.type_value = type_value;
//            }
//
//            public int getSupply_material_price_list_id() {
//                return supply_material_price_list_id;
//            }
//
//            public void setSupply_material_price_list_id(int supply_material_price_list_id) {
//                this.supply_material_price_list_id = supply_material_price_list_id;
//            }
//
//            public List<?> getChildren() {
//                return children;
//            }
//
//            public void setChildren(List<?> children) {
//                this.children = children;
//            }
//        }
//    }
}
