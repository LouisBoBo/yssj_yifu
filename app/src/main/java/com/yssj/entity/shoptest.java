package com.yssj.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class shoptest implements Serializable {

    private int cart_count;
    private ShopDTO shop;
    private String work_count;
    private String type_count;
    private String color_count;
    private long s_time;
    private int c_time;
    private String eva_count;
    private String cost_count;
    private String star_count;
    private String status;
    private List<AttrListDTO> attrList;

    public int getCart_count() {
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public void setShop(ShopDTO shop) {
        this.shop = shop;
    }

    public String getWork_count() {
        return work_count;
    }

    public void setWork_count(String work_count) {
        this.work_count = work_count;
    }

    public String getType_count() {
        return type_count;
    }

    public void setType_count(String type_count) {
        this.type_count = type_count;
    }

    public String getColor_count() {
        return color_count;
    }

    public void setColor_count(String color_count) {
        this.color_count = color_count;
    }

    public long getS_time() {
        return s_time;
    }

    public void setS_time(long s_time) {
        this.s_time = s_time;
    }

    public int getC_time() {
        return c_time;
    }

    public void setC_time(int c_time) {
        this.c_time = c_time;
    }

    public String getEva_count() {
        return eva_count;
    }

    public void setEva_count(String eva_count) {
        this.eva_count = eva_count;
    }

    public String getCost_count() {
        return cost_count;
    }

    public void setCost_count(String cost_count) {
        this.cost_count = cost_count;
    }

    public String getStar_count() {
        return star_count;
    }

    public void setStar_count(String star_count) {
        this.star_count = star_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AttrListDTO> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<AttrListDTO> attrList) {
        this.attrList = attrList;
    }

    public static class ShopDTO implements Serializable {
        private String def_pic;
        private String color;
        private String shop_code;
        private String shop_attr;
        private String supply_start_time;
        private String invertory_num;
        private String content;
        private double app_shop_group_price;
        private String random;
        private String supply_batch_num;
        private String p_code;
        private String four_pic;
        private double wxcx_shop_group_price;
        private String supply_min_num;
        private String is_hot;
        private String shop_group_price;
        private String supp_label_id;
        private String kickback;
        private String virtual_sales;
        private String advance_sale_days;
        private String shop_tag;
        private String shop_price;
        private String shop_status;
        private String is_new;
        private String supply_end_time;
        private String shop_discount_time;
        private String shop_kind;
        private String supp_label;
        private String qr_pic;
        private String supp_id;
        private String shop_se_price;
        private String shop_name;
        private String type1;
        private double assmble_price;
        private String shelf;
        private String supply_current_num;
        private String shop_pic;
        private SupplierLabelDTO supplier_label;
        private String clicks;
        private String actual_sales;
        private String add_time;
        private String shop_up_time;
        private List<SupplyMaterialPriceListDTO> supply_material_price_list;

        public String getDef_pic() {
            return def_pic;
        }

        public void setDef_pic(String def_pic) {
            this.def_pic = def_pic;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getShop_code() {
            return shop_code;
        }

        public void setShop_code(String shop_code) {
            this.shop_code = shop_code;
        }

        public String getShop_attr() {
            return shop_attr;
        }

        public void setShop_attr(String shop_attr) {
            this.shop_attr = shop_attr;
        }

        public String getSupply_start_time() {
            return supply_start_time;
        }

        public void setSupply_start_time(String supply_start_time) {
            this.supply_start_time = supply_start_time;
        }

        public String getInvertory_num() {
            return invertory_num;
        }

        public void setInvertory_num(String invertory_num) {
            this.invertory_num = invertory_num;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public double getApp_shop_group_price() {
            return app_shop_group_price;
        }

        public void setApp_shop_group_price(double app_shop_group_price) {
            this.app_shop_group_price = app_shop_group_price;
        }

        public String getRandom() {
            return random;
        }

        public void setRandom(String random) {
            this.random = random;
        }

        public String getSupply_batch_num() {
            return supply_batch_num;
        }

        public void setSupply_batch_num(String supply_batch_num) {
            this.supply_batch_num = supply_batch_num;
        }

        public String getP_code() {
            return p_code;
        }

        public void setP_code(String p_code) {
            this.p_code = p_code;
        }

        public String getFour_pic() {
            return four_pic;
        }

        public void setFour_pic(String four_pic) {
            this.four_pic = four_pic;
        }

        public double getWxcx_shop_group_price() {
            return wxcx_shop_group_price;
        }

        public void setWxcx_shop_group_price(double wxcx_shop_group_price) {
            this.wxcx_shop_group_price = wxcx_shop_group_price;
        }

        public String getSupply_min_num() {
            return supply_min_num;
        }

        public void setSupply_min_num(String supply_min_num) {
            this.supply_min_num = supply_min_num;
        }

        public String getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(String is_hot) {
            this.is_hot = is_hot;
        }

        public String getShop_group_price() {
            return shop_group_price;
        }

        public void setShop_group_price(String shop_group_price) {
            this.shop_group_price = shop_group_price;
        }

        public String getSupp_label_id() {
            return supp_label_id;
        }

        public void setSupp_label_id(String supp_label_id) {
            this.supp_label_id = supp_label_id;
        }

        public String getKickback() {
            return kickback;
        }

        public void setKickback(String kickback) {
            this.kickback = kickback;
        }

        public String getVirtual_sales() {
            return virtual_sales;
        }

        public void setVirtual_sales(String virtual_sales) {
            this.virtual_sales = virtual_sales;
        }

        public String getAdvance_sale_days() {
            return advance_sale_days;
        }

        public void setAdvance_sale_days(String advance_sale_days) {
            this.advance_sale_days = advance_sale_days;
        }

        public String getShop_tag() {
            return shop_tag;
        }

        public void setShop_tag(String shop_tag) {
            this.shop_tag = shop_tag;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getSupply_end_time() {
            return supply_end_time;
        }

        public void setSupply_end_time(String supply_end_time) {
            this.supply_end_time = supply_end_time;
        }

        public String getShop_discount_time() {
            return shop_discount_time;
        }

        public void setShop_discount_time(String shop_discount_time) {
            this.shop_discount_time = shop_discount_time;
        }

        public String getShop_kind() {
            return shop_kind;
        }

        public void setShop_kind(String shop_kind) {
            this.shop_kind = shop_kind;
        }

        public String getSupp_label() {
            return supp_label;
        }

        public void setSupp_label(String supp_label) {
            this.supp_label = supp_label;
        }

        public String getQr_pic() {
            return qr_pic;
        }

        public void setQr_pic(String qr_pic) {
            this.qr_pic = qr_pic;
        }

        public String getSupp_id() {
            return supp_id;
        }

        public void setSupp_id(String supp_id) {
            this.supp_id = supp_id;
        }

        public String getShop_se_price() {
            return shop_se_price;
        }

        public void setShop_se_price(String shop_se_price) {
            this.shop_se_price = shop_se_price;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public double getAssmble_price() {
            return assmble_price;
        }

        public void setAssmble_price(double assmble_price) {
            this.assmble_price = assmble_price;
        }

        public String getShelf() {
            return shelf;
        }

        public void setShelf(String shelf) {
            this.shelf = shelf;
        }

        public String getSupply_current_num() {
            return supply_current_num;
        }

        public void setSupply_current_num(String supply_current_num) {
            this.supply_current_num = supply_current_num;
        }

        public String getShop_pic() {
            return shop_pic;
        }

        public void setShop_pic(String shop_pic) {
            this.shop_pic = shop_pic;
        }

        public SupplierLabelDTO getSupplier_label() {
            return supplier_label;
        }

        public void setSupplier_label(SupplierLabelDTO supplier_label) {
            this.supplier_label = supplier_label;
        }

        public String getClicks() {
            return clicks;
        }

        public void setClicks(String clicks) {
            this.clicks = clicks;
        }

        public String getActual_sales() {
            return actual_sales;
        }

        public void setActual_sales(String actual_sales) {
            this.actual_sales = actual_sales;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getShop_up_time() {
            return shop_up_time;
        }

        public void setShop_up_time(String shop_up_time) {
            this.shop_up_time = shop_up_time;
        }

        public List<SupplyMaterialPriceListDTO> getSupply_material_price_list() {
            return supply_material_price_list;
        }

        public void setSupply_material_price_list(List<SupplyMaterialPriceListDTO> supply_material_price_list) {
            this.supply_material_price_list = supply_material_price_list;
        }

        public static class SupplierLabelDTO implements Serializable {
            private IdDTO _id;
            private int id;
            private String icon;
            private int is_show;
            private int sort;
            private String remark;
            private String name;
            private long add_time;
            private String pic;
            private int type;
            private long update_time;

            public IdDTO get_id() {
                return _id;
            }

            public void set_id(IdDTO _id) {
                this._id = _id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getAdd_time() {
                return add_time;
            }

            public void setAdd_time(long add_time) {
                this.add_time = add_time;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public static class IdDTO implements Serializable {
                private long time;
                private int timestamp;
                private long date;
                private int timeSecond;
                private int inc;
                private int machine;
                @SerializedName("new")
                private boolean newX;

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public int getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(int timestamp) {
                    this.timestamp = timestamp;
                }

                public long getDate() {
                    return date;
                }

                public void setDate(long date) {
                    this.date = date;
                }

                public int getTimeSecond() {
                    return timeSecond;
                }

                public void setTimeSecond(int timeSecond) {
                    this.timeSecond = timeSecond;
                }

                public int getInc() {
                    return inc;
                }

                public void setInc(int inc) {
                    this.inc = inc;
                }

                public int getMachine() {
                    return machine;
                }

                public void setMachine(int machine) {
                    this.machine = machine;
                }

                public boolean isNewX() {
                    return newX;
                }

                public void setNewX(boolean newX) {
                    this.newX = newX;
                }
            }
        }


        public static class SupplyMaterialPriceListDTO implements Serializable {
            private String type_name;
            private int parent_id;
            private int id;
            private long create_date;
            private int supply_material_id;
            private int supply_material_price_list_id;
            private int type_key;
            private List<ChildrenDTO> children;

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getCreate_date() {
                return create_date;
            }

            public void setCreate_date(long create_date) {
                this.create_date = create_date;
            }

            public int getSupply_material_id() {
                return supply_material_id;
            }

            public void setSupply_material_id(int supply_material_id) {
                this.supply_material_id = supply_material_id;
            }

            public int getSupply_material_price_list_id() {
                return supply_material_price_list_id;
            }

            public void setSupply_material_price_list_id(int supply_material_price_list_id) {
                this.supply_material_price_list_id = supply_material_price_list_id;
            }

            public int getType_key() {
                return type_key;
            }

            public void setType_key(int type_key) {
                this.type_key = type_key;
            }

            public List<ChildrenDTO> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenDTO> children) {
                this.children = children;
            }

            public static class ChildrenDTO implements Serializable {
                private String type_name;
                private int parent_id;
                private int price;
                private int type_use;
                private int id;
                private long create_date;
                private int supply_material_id;
                private String type_value;
                private int supply_material_price_list_id;
                private List<?> children;

                public String getType_name() {
                    return type_name;
                }

                public void setType_name(String type_name) {
                    this.type_name = type_name;
                }

                public int getParent_id() {
                    return parent_id;
                }

                public void setParent_id(int parent_id) {
                    this.parent_id = parent_id;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public int getType_use() {
                    return type_use;
                }

                public void setType_use(int type_use) {
                    this.type_use = type_use;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public long getCreate_date() {
                    return create_date;
                }

                public void setCreate_date(long create_date) {
                    this.create_date = create_date;
                }

                public int getSupply_material_id() {
                    return supply_material_id;
                }

                public void setSupply_material_id(int supply_material_id) {
                    this.supply_material_id = supply_material_id;
                }

                public String getType_value() {
                    return type_value;
                }

                public void setType_value(String type_value) {
                    this.type_value = type_value;
                }

                public int getSupply_material_price_list_id() {
                    return supply_material_price_list_id;
                }

                public void setSupply_material_price_list_id(int supply_material_price_list_id) {
                    this.supply_material_price_list_id = supply_material_price_list_id;
                }

                public List<?> getChildren() {
                    return children;
                }

                public void setChildren(List<?> children) {
                    this.children = children;
                }
            }
        }
    }

    public static class AttrListDTO implements Serializable {
        private String color_type;
        private String id;
        private String ico;
        private String parent_id;
        private String is_show;
        private String attr_name;

        public String getColor_type() {
            return color_type;
        }

        public void setColor_type(String color_type) {
            this.color_type = color_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getAttr_name() {
            return attr_name;
        }

        public void setAttr_name(String attr_name) {
            this.attr_name = attr_name;
        }
    }
}
