package com.yssj.ui.fragment.contributions;

import java.io.Serializable;
import java.util.List;

public class SupplyLogistBean implements Serializable {

    private String message;
    private String status;
    private List<DataDTOX> data;

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

    public List<DataDTOX> getData() {
        return data;
    }

    public void setData(List<DataDTOX> data) {
        this.data = data;
    }

    public static class DataDTOX implements Serializable {
        private LastResultDTO lastResult;
        private String order_code;
        private String status;
        private long end_push_time;

        public LastResultDTO getLastResult() {
            return lastResult;
        }

        public void setLastResult(LastResultDTO lastResult) {
            this.lastResult = lastResult;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getEnd_push_time() {
            return end_push_time;
        }

        public void setEnd_push_time(long end_push_time) {
            this.end_push_time = end_push_time;
        }

        public static class LastResultDTO implements Serializable {
            private List<DataDTO> data;

            public List<DataDTO> getData() {
                return data;
            }

            public void setData(List<DataDTO> data) {
                this.data = data;
            }

            public static class DataDTO implements Serializable {
                private String time;
                private String context;
                private String ftime;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getContext() {
                    return context;
                }

                public void setContext(String context) {
                    this.context = context;
                }

                public String getFtime() {
                    return ftime;
                }

                public void setFtime(String ftime) {
                    this.ftime = ftime;
                }
            }
        }
    }
}
