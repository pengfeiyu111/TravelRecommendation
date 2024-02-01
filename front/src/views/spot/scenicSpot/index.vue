<template>
<div class="search">
    <add v-if="currView=='add'" @close="currView='index'" @submited="submited" />
    <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :data="formData" />
    <Card v-show="currView=='index'">
        <Row v-show="openSearch" @keydown.enter.native="handleSearch">
            <Form ref="searchForm" :model="searchForm" inline :label-width="0">
                <Form-item ref="searchForm" :model="searchForm" inline :label-width="0" style="display:flex;">
                    <Form-item label="" prop="title">
                        <Input type="text" v-model="searchForm.title" placeholder="景点名称" clearable style="width: 150px" />
                    </Form-item>
                    <Form-item label="" prop="type">
                        <Select v-model="searchForm.type" clearable placeholder="景点类型" style="width:150px">
                            <Option v-for="(item,index) in typeList" :key="index" :value="item.title">{{ item.title }}</Option>
                        </Select>
                    </Form-item>
                    <Form-item label="" prop="address">
                        <Input type="text" v-model="searchForm.address" placeholder="景区地址" clearable style="width: 150px" />
                    </Form-item>
                    <Form-item style="margin-left:10px;" class="br">
                        <Button @click="handleSearch" type="primary" icon="ios-search" size="small" ghost>搜索</Button>
                        <Button @click="handleReset" type="warning" size="small" icon="md-refresh" ghost>重置</Button>
                        <Button @click="add" type="info" size="small" icon="md-add" ghost :disabled="!$route.meta.permTypes.includes('add')">添加</Button>
                        <Button @click="delAll" type="error" icon="md-trash" size="small" ghost :disabled="!$route.meta.permTypes.includes('delete')">删除</Button>
                        <Button @click="excelData" type="success" icon="md-paper-plane" size="small" ghost>导出</Button>
                    </Form-item>
                    <Form-item style="position:fixed;right:50px;top:130px">
                        <Button type="info" @click="showFilterPanelFlag = !showFilterPanelFlag" class="showFilterPanelFlag" icon="md-settings" size="small" ghost>
                            列筛选</Button>
                        <Button type="warning" @click="modal1 = true" class="showFilterPanelFlag" icon="ios-help-circle-outline" size="small" ghost>
                            使用教程</Button>
                        <Modal v-model="modal1" title="使用教程">
                            <p>1.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>2.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>3.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                        </Modal>
                    </Form-item>
                </Form-item>
            </Form>
        </Row>
        <Row class="operation" style="position:relative;">
            <transition>
                <div v-show="showFilterPanelFlag" class="filter-panel">
                    <CheckboxGroup v-model="selected">
                        <div v-for="item in mycolumns" :key="item.key">
                            <Checkbox :label="item.title" style="margin: 2px 5px"></Checkbox>
                        </div>
                    </CheckboxGroup>
                </div>
            </transition>
        </Row>
        <Row v-show="openTip"> </Row>
        <Row :gutter="16">
            <Col span="24">
            <Table :loading="loading" :height="tableHeight" border stripe size="small" :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect" @on-row-click="rowClick" :row-class-name="rowClassNmae"></Table>
            </Col>
        </Row>
        <Row type="flex" justify="end" class="page">
            <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[15,20,50]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
    </Card>
    <Modal v-model="evaluateModal" :title="evaluateTitle" draggable footer-hide mask="false" width="1200">
        <Form :label-width="100" label-position="left">
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="景点名称">
                    <Input v-model="selectMyRow.title" readonly show-word-limit maxlength="240" style="width:100%" />
                </FormItem>
                </Col>
                <Col span="24">
                <FormItem label="我的评分">
                    <dict dict="spot_star" v-model="evaluateValue" transfer clearable placeholder="请选择您的评分..." style="width: 100%" />
                </FormItem>
                </Col>
                <Col span="24">
                <FormItem label="评语">
                    <Input v-model="evaluateMessage" clearable type="textarea" :rows="4" show-word-limit maxlength="240" placeholder="请输入您的评语..." style="width:100%" />
                </FormItem>
                </Col>
                <Col span="24">
                <Divider>
                    <Button @click="addEvaluateFx" :loading="evaluateLoading" type="success">提交并保存</Button>
                </Divider>
                </Col>
            </Row>
        </Form>
    </Modal>
</div>
</template>

<script>
import {
    getScenicSpotList,
    deleteScenicSpot,
    addCollection,
    cancelCollection,
    addEvaluate,
    getScenicSpotTypeList
} from "./api.js";
import add from "./add.vue";
import edit from "./edit.vue";
import dict from "@/views/template/dict";
export default {
    name: "single-window",
    components: {
        add,
        edit,
        dict
    },
    data() {
        return {
            evaluateModal: false,
            evaluateLoading: false,
            evaluateTitle: "",
            evaluateValue: "0",
            evaluateMessage: "",
            selectMyRow: {},
            tableHeight: 0,
            selected: [
                "选择",
                "序号",
                "景点名称",
                "景点类型",
                "评分",
                "收藏量",
                "景点图片",
                "门票价格",
                "门票预订",
                "开放时间",
                "景区地址",
                "景点介绍",
                "操作",
            ],
            modal1: false,
            openSearch: true, // 显示搜索
            openTip: true, // 显示提示
            formData: {},
            currView: "index",
            loading: true, // 表单加载状态
            searchForm: { // 搜索框初始化对象
                pageNumber: 1, // 当前页数
                pageSize: 15, // 页面大小
                sort: "createTime", // 默认排序字段
                order: "desc", // 默认排序方式
            },
            selectList: [], // 多选数据
            selectCount: 0, // 多选计数
            selectRow: 0,
            columns: [
                // 表头
                {
                    type: "selection",
                    width: 60,
                    title: "选择",
                    align: "center",
                    fixed: "left",
                },
                {
                    title: "序号",
                    width: 85,
                    align: "center",
                    fixed: "left",
                    sortType: true,
                    render: (h, params) => {
                        return h(
                            "span",
                            params.index +
                            (this.searchForm.pageNumber - 1) * this.searchForm.pageSize +
                            1
                        );
                    },
                },
                {
                    title: "景点名称",
                    key: "title",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "景点类型",
                    key: "type",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "评分",
                    key: "star",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                    render: (h, params) => {
                        if (params.row.star == -1) {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#ff0000",
                                        },
                                    },
                                    "未被打分"
                                ),
                            ]);
                        } else {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#3CB371",
                                        },
                                    },
                                    params.row.star + "分"
                                ),
                            ]);
                        }
                    },
                },
                {
                    title: "收藏量",
                    key: "collection",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                    render: (h, params) => {
                        return h("div", [
                            h(
                                "span",
                                params.row.collection + "次"
                            ),
                        ]);
                    },
                },
                {
                    title: "门票价格",
                    key: "price",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "门票预订",
                    key: "url",
                    minWidth: 140,
                    tooltip: true,
                    sortable: false,
                    render: (h, params) => {
                        if (params.row.url != undefined && params.row.url != "") {
                            return h("div", [
                                h(
                                    "Button", {
                                        props: {
                                            type: "primary",
                                            size: "small",
                                            icon: "ios-create-outline",
                                            ghost: true
                                        },
                                        style: {
                                            marginRight: "5px"
                                        },
                                        on: {
                                            click: () => {
                                                window.open(params.row.url);
                                            }
                                        }
                                    },
                                    "点我预定"
                                )
                            ]);
                        } else {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#ff0000",
                                        },
                                    },
                                    "无法预定"
                                ),
                            ]);
                        }
                    }
                },
                {
                    title: "开放时间",
                    key: "openTime",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "景区地址",
                    key: "address",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "景点介绍",
                    key: "content",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "景点图片",
                    key: "image",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                    render: (h, params) => {
                        if (params.row.image == undefined || params.row.image == "") {
                            return h("div", [
                                h(
                                    "span", {
                                        style: {
                                            color: "#ff9900",
                                        },
                                    },
                                    "未上传"
                                ),
                            ]);
                        } else {
                            return h("img", {
                                attrs: {
                                    src: params.row.image,
                                },
                                style: {
                                    cursor: "zoom-in",
                                    width: "60px",
                                    margin: "10px 0",
                                    "object-fit": "contain",
                                },
                                on: {
                                    click: () => {
                                        this.showPic(params.row.image);
                                    },
                                },
                            });
                        }
                    }
                },
                {
                    title: "创建时间",
                    key: "createTime",
                    sortable: true,
                    sortType: "desc",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "创建者",
                    key: "createBy",
                    sortable: true,
                    sortType: "desc",
                    minWidth: 100,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "修改时间",
                    key: "updateTime",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "修改者",
                    key: "updateBy",
                    minWidth: 100,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "操作",
                    key: "action",
                    align: "center",
                    width: 370,
                    fixed: "right",
                    render: (h, params) => {
                        var that = this;
                        return h("div", [
                            h(
                                "Button", {
                                    props: {
                                        type: "success",
                                        size: "small",
                                        icon: "ios-settings",
                                        ghost: true
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.selectMyRow = params.row;
                                            this.evaluateValue = "" + this.selectMyRow.myEvaluate;
                                            this.evaluateMessage = this.selectMyRow.myEvaluateMessage;
                                            this.evaluateTitle = params.row.title + " 评分";
                                            this.evaluateModal = true;
                                        }
                                    }
                                },
                                "评价"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: params.row.myCollection ? "warning" : "success",
                                        size: "small",
                                        icon: "ios-create-outline",
                                        ghost: true
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            if (params.row.myCollection) {
                                                this.cancelCollectionFx(params.row)
                                            } else {
                                                this.addCollectionFx(params.row)
                                            }
                                        }
                                    }
                                },
                                params.row.myCollection ? "取消收藏" : "收藏",
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "primary",
                                        size: "small",
                                        icon: "ios-create-outline",
                                        disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("edit")),
                                        ghost: true
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.edit(params.row);
                                        }
                                    }
                                },
                                "编辑"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "error",
                                        size: "small",
                                        icon: "md-trash",
                                        disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("delete")),
                                        ghost: true
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.row);
                                        }
                                    }
                                },
                                "删除"
                            )
                        ]);
                    }
                }
            ],
            data: [], // 表单数据
            pageNumber: 1, // 当前页数
            pageSize: 10, // 页面大小
            total: 0, // 表单数据总数
            showFilterPanelFlag: false,
            typeList: []
        };
    },
    methods: {
        init() {
            this.getDataList();
            this.getScenicSpotTypeListFx();
        },
        getScenicSpotTypeListFx() {
            var that = this;
            that.typeList = [];
            getScenicSpotTypeList().then(res => {
                if (res.success) {
                    that.typeList = res.result;
                }
            })
        },
        addEvaluateFx() {
            var that = this;
            that.evaluateLoading = true;
            addEvaluate({
                id: that.selectMyRow.id,
                level: that.evaluateValue,
                message: that.evaluateMessage
            }).then(res => {
                that.evaluateLoading = false;
                if (res.success) {
                    that.evaluateModal = false;
                    this.$Message.success("评分成功");
                    that.getDataList();
                }
            })
        },
        cancelCollectionFx(e) {
            var that = this;
            cancelCollection({
                id: e.id
            }).then(res => {
                if (res.success) {
                    this.$Message.success("取消收藏成功");
                    that.getDataList();
                }
            })
        },
        addCollectionFx(e) {
            var that = this;
            addCollection({
                id: e.id
            }).then(res => {
                if (res.success) {
                    this.$Message.success("收藏成功");
                    that.getDataList();
                }
            })
        },
        submited() {
            this.currView = "index";
            this.getDataList();
        },
        changePage(v) {
            this.searchForm.pageNumber = v;
            this.getDataList();
            this.clearSelectAll();
        },
        changePageSize(v) {
            this.searchForm.pageSize = v;
            this.getDataList();
        },
        rowClick(row, index) {
            this.selectRow = row;
        },
        rowClassNmae(row, index) {
            if (row.id == this.selectRow.id) {
                return "rowClassNmaeColor";
            }
            return "";
        },
        excelData() {
            this.$refs.table.exportCsv({
                filename: "导出结果",
            });
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.getDataList();
        },
        handleReset() {
            this.$refs.searchForm.resetFields();
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            // 重新加载数据
            this.getDataList();
        },
        changeSort(e) {
            this.searchForm.sort = e.key;
            this.searchForm.order = e.order;
            if (e.order === "normal") {
                this.searchForm.order = "";
            }
            this.getDataList();
        },
        clearSelectAll() {
            this.$refs.table.selectAll(false);
        },
        changeSelect(e) {
            this.selectList = e;
            this.selectCount = e.length;
        },
        getDataList() {
            this.loading = true;
            getScenicSpotList(this.searchForm).then(res => {
                this.loading = false;
                if (res.success) {
                    this.data = res.result.records;
                    this.total = res.result.total;
                }
            });
        },
        add() {
            this.currView = "add";
        },
        edit(v) {
            // 转换null为""
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let data = JSON.parse(str);
            this.formData = data;
            this.currView = "edit";
        },
        remove(v) {
            this.$Modal.confirm({
                title: "确认删除",
                // 记得确认修改此处
                content: "您确认要删除 " + v.name + " ?",
                loading: true,
                onOk: () => {
                    // 删除
                    deleteScenicSpot({
                        ids: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.getDataList();
                        }
                    });
                }
            });
        },
        delAll() {
            if (this.selectCount <= 0) {
                this.$Message.warning("您还未选择要删除的数据");
                return;
            }
            this.$Modal.confirm({
                title: "确认删除",
                content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
                loading: true,
                onOk: () => {
                    let ids = "";
                    this.selectList.forEach(function (e) {
                        ids += e.id + ",";
                    });
                    ids = ids.substring(0, ids.length - 1);
                    // 批量删除
                    deleteScenicSpot({
                        ids: ids
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.clearSelectAll();
                            this.getDataList();
                        }
                    });
                }
            });
        },
        showPic(e) {
            window.open(e + "?preview=true");
        }
    },
    mounted() {
        this.init();
        this.tableHeight = Number(window.innerHeight - 273);
        this.mycolumns = this.columns;
        let showcolumns = [];
        for (var i = 0; i < this.selected.length; i++) {
            var item = this.selected[i];
            for (var j = 0; j < this.columns.length; j++) {
                if (this.columns[j].title == item) {
                    showcolumns.push(this.columns[j]);
                }
            }
        }
        this.columns = showcolumns;
    },
    watch: {
        selected: function (newcolumns) {
            let showcolumns = [];
            for (var i = 0; i < this.mycolumns.length; i++) {
                var item = this.mycolumns[i];
                if (item.title == undefined) showcolumns.push(item);
                else if (newcolumns.includes(item.title)) showcolumns.push(item);
            }
            this.columns = showcolumns;
        },
    },
};
</script>

<style lang="less">
// @import "../../../styles/table-common.less";
.search {
    .operation {
        margin-bottom: 2vh;
    }

    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }

    .select-clear {
        margin-left: 10px;
    }

    .page {
        margin-top: 2vh;
    }

    .drop-down {
        margin-left: 5px;
    }
}

.filter-panel {
    width: 166px;
    min-height: 120px;
    height: 200px;
    position: absolute;
    background-color: white;
    z-index: 9999;
    margin-left: 1px;
    overflow-y: scroll;
    border: 1px solid blue;
    top: 35px;
    right: 10px;
}

.openSearch {
    position: absolute;
    right: 240px;
}

.openTip {
    position: absolute;
    right: 130px;
}

.showFilterPanelFlag {
    position: static !important;
    right: 10px;
    margin-right: 10px;
}

.ivu-table td {
    height: 38px !important;
}

.ivu-table-cell-with-expand {
    height: 38px !important;
    line-height: 38px !important;
}

.ivu-table .rowClassNmaeColor td {
    background-color: #b0b3b6 !important;
    color: #ffffff !important;
    font-size: 12px;
}
</style>
