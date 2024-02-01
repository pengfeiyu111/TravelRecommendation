<template>
    <div>
        <Card>
            <Row :gutter="16">
                <span style="font-weight: 550;font-size: 26px;">海南旅游景点评分 TOP 10</span>
            </Row>
            <Row :gutter="16">
                <Col span="24">
                <div id="container31"></div>
                </Col>
            </Row>
        </Card>
    </div>
    </template>
    
    <script>
    import {
        getTopEvaluate
    } from "./api.js";
    import {
        Bar
    } from '@antv/g2plot';
    export default {
        name: "test-page",
        components: {},
        props: {},
        data() {
            return {
                stackedBarPlot: {},
                userData: [{
                    title: 'AAA',
                    value: 6,
                }, {
                    title: 'BBB',
                    value: 4,
                }, ]
            }
        },
        methods: {
            init() {
                this.getTopEvaluateFx();
            },
            getTopEvaluateFx() {
                var that = this;
                var data = [];
                getTopEvaluate().then(res => {
                    console.log(res);
                    if (res.success) {
                        for (var i = 0; i < res.result.length; i++) {
                            var item = {};
                            item.title = res.result[i].title;
                            item.value = res.result[i].star;
                            data.push(item);
                        }
                        that.stackedBarPlot = new Bar('container31', {
                            data,
                            xField: 'value',
                            yField: 'title',
                            seriesField: 'title',
                            legend: {
                                position: 'top-left',
                            },
                        });
                        that.stackedBarPlot.render();
                    }
                })
            },
            initAntvFx() {
                var data = this.userData;
                this.stackedBarPlot = new Bar('container30', {
                    data,
                    xField: 'value',
                    yField: 'title',
                    seriesField: 'title',
                    legend: {
                        position: 'top-left',
                    },
                });
                this.stackedBarPlot.render();
            },
        },
        mounted() {
            this.init();
        },
    };
    </script>
    
    <style lang="less" scoped>
    #container31 {
        width: 100%;
        height: 500px;
        margin-top: 20px;
        padding-left: 10px;
        padding-right: 10px;
    }
    
    .antvTitle {
        font-size: 26px;
        text-align: center;
        justify-content: center;
        display: flex;
    }
    </style>
    