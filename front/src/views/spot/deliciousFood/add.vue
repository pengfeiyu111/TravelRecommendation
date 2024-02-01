<template>
<div>
    <Card>
        <div slot="title">
            <div class="edit-head">
                <a @click="close" class="back-title">
                    <Icon type="ios-arrow-back" />返回
                </a>
                <div class="head-name">添加景点美食</div>
                <span></span>
                <a @click="close" class="window-close">
                    <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
                </a>
            </div>
        </div>
        <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left">
            <FormItem label="所属景点" prop="spotId">
                <Select v-model="form.spotId" placeholder="请选择所属景点..." style="width:570px">
                    <Option v-for="(item,index) in spotList" :value="item.id" :key="index">{{ item.title }}</Option>
                </Select>
            </FormItem>
            <FormItem label="美食名称" prop="title">
                <Input v-model="form.title" show-word-limit maxlength="240" clearable placeholder="请输入美食名称..." style="width:570px" />
            </FormItem>
            <FormItem label="美食图片" prop="image">
                <upload-pic-input v-model="form.image" placeholder="请上传美食图片..." style="width:570px"></upload-pic-input>
            </FormItem>
            <FormItem label="类型" prop="type">
                <dict dict="food_type" v-model="form.type" transfer clearable placeholder="请选择美食类型..." style="width: 570px" />
            </FormItem>
            <FormItem label="简介" prop="content">
                <Input v-model="form.content" type="textarea" :rows="4" show-word-limit maxlength="240" clearable placeholder="请输入美食简介..." style="width:570px" />
            </FormItem>
            <FormItem label="人均消费" prop="avg">
                <InputNumber v-model="form.avg" min="0" max="5000000" style="width:570px"></InputNumber>
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
    addDeliciousFood,
    getScenicSpotList
} from "./api.js";
import uploadPicInput from "@/views/template/upload-pic-input";
import dict from "@/views/template/dict";
export default {
    name: "add",
    components: {
        uploadPicInput,
        dict
    },
    data() {
        return {
            submitLoading: false, // 表单提交状态
            form: { // 添加或编辑表单对象初始化数据
                title: "",
                image: "",
                type: "",
                content: "",
                avg: 0,
            },
            // 表单验证规则
            formValidate: {},
            spotList: []
        };
    },
    methods: {
        init() {
            this.getScenicSpotListFx();
        },
        getScenicSpotListFx() {
            var that = this;
            that.spotList = [];
            getScenicSpotList().then(res => {
                if (res.success) {
                    that.spotList = res.result;
                }
            })
        },
        handleReset() {
            this.$refs.form.resetFields();
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    addDeliciousFood(this.form).then(res => {
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
