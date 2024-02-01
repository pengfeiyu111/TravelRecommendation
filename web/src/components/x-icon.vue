<template>
  <i style="display: inline-block">
    <van-icon
      v-if="checkIcon(type) == 'icon'"
      name="setting-o"
      :size="size"
      :color="color"
    />
    <van-icon
      v-if="checkIcon(type) == 'iconfont'"
      :custom="type"
      :size="size"
      :color="color"
    />
    <van-image
      v-show="checkIcon(type) == 'link'"
      :src="imageUrl"
      :width="size + 'px'"
      :height="size + 'px'"
      style="vertical-align: -0.125em"
    />
  </i>
</template>

<script>
export default {
  name: "x-icon",
  props: {
    type: String,
    size: {
      type: [Number, String],
      default: 60,
    },
    color: String,
  },
  data() {
    return {
      imageUrl: ""
    };
  },
  methods: {
    checkIcon(v) {
      if (!v) {
        return "null";
      }
      if (
        v.indexOf("http://") == 0 ||
        v.indexOf("https://") == 0 ||
        v.indexOf(";base64,") > 0
      ) {
        this.imageUrl = v + "?zwzCode=1"
        return "link";
      } else if (v.indexOf("iconfont") > -1) {
        return "iconfont";
      } else {
        return "icon";
      }
    },
  },
  watch: {},
  mounted() {},
};
</script>

