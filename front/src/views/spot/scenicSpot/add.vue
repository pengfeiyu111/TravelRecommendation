<template>
<div>
    <Card>
        <div slot="title">
            <div class="edit-head">
                <a @click="close" class="back-title">
                    <Icon type="ios-arrow-back" />返回
                </a>
                <div class="head-name">添加</div>
                <span></span>
                <a @click="close" class="window-close">
                    <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
                </a>
            </div>
        </div>
        <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left">
            <FormItem label="景点名称" prop="title">
                <Input v-model="form.title" clearable placeholder="请输入景点名称..." show-word-limit maxlength="240" style="width:570px" />
            </FormItem>
            <FormItem label="景点类型" prop="type">
                <Select v-model="form.type" clearable placeholder="请选择景点类型..." style="width:570px">
                    <Option v-for="(item,index) in typeList" :key="index" :value="item.title">{{ item.title }}</Option>
                </Select>
            </FormItem>
            <FormItem label="门票价格" prop="price">
                <InputNumber v-model="form.price" min="0" max="5000000" placeholder="请输入门票价格..." style="width:570px"></InputNumber>
            </FormItem>
            <FormItem label="门票预订网址" prop="url">
                <Input v-model="form.url" clearable placeholder="请输入门票预订网址..." show-word-limit maxlength="240" style="width:570px" />
            </FormItem>
            <FormItem label="开放时间" prop="openTime">
                <Input v-model="form.openTime" clearable placeholder="请输入开放时间..." show-word-limit maxlength="240" style="width:570px" />
            </FormItem>
            <FormItem label="景区地址" prop="address">
                <Input v-model="form.address" clearable placeholder="请输入景区地址..." show-word-limit maxlength="240" style="width:570px" />
            </FormItem>
            <FormItem label="景点介绍" prop="content">
                <Input v-model="form.content" clearable type="textarea" :rows="4" show-word-limit maxlength="240" placeholder="请输入景点介绍..." style="width:570px" />
            </FormItem>
            <FormItem label="景点图片" prop="image">
                <upload-pic-input v-model="form.image" placeholder="请上传景点图片..." style="width:570px"></upload-pic-input>
            </FormItem>
            <Form-item class="br">
                <Button @click="handleSubmit" :loading="submitLoading" type="primary">提交并保存</Button>
                <Button @click="handleReset">重置</Button>
                <Button type="dashed" @click="close">关闭</Button>
            </Form-item>
        </Form>
    </Card>
</div>
</template>

<script>
import {
    addScenicSpot,
    getScenicSpotTypeList
} from "./api.js";
import uploadPicInput from "@/views/template/upload-pic-input";
export default {
    name: "add",
    components: {
        uploadPicInput,
    },
    data() {
        return {
            submitLoading: false, // 表单提交状态
            form: { // 添加或编辑表单对象初始化数据
                title: "",
                type: "",
                star: 0,
                collection: 0,
                price: 0,
                url: "",
                openTime: "",
                address: "",
                content: "",
                image: "",
            },
            // 表单验证规则
            formValidate: {},
            typeList: []
        };
    },
    methods: {
        init() {
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
        handleReset() {
            this.$refs.form.resetFields();
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    addScenicSpot(this.form).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.submited();
                        }
                    });
                }
            });
        },
        close() {
            this.$emit("close", true);
        },
        submited() {
            this.$emit("submited", true);
        }
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/single-common.less";
.edit-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: relative;

    .back-title {
        color: #515a6e;
        display: flex;
        align-items: center;
    }

    .head-name {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 16px;
        color: #17233d;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .window-close {
        z-index: 1;
        font-size: 12px;
        position: absolute;
        right: 0px;
        top: -5px;
        overflow: hidden;
        cursor: pointer;

        .ivu-icon-ios-close {
            color: #999;
            transition: color .2s ease;
        }
    }

    .window-close .ivu-icon-ios-close:hover {
        color: #444;
    }
}
</style>
