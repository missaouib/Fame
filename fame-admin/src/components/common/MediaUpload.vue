<template>
  <div class="upload-component">
    <el-button
      type="primary"
      icon="el-icon-upload"
      @click="uploadVisible = !uploadVisible"
    >
      <span v-if="uploadVisible">隐藏</span>
      <span v-else>上传</span>
    </el-button>
    <el-button type="info" v-show="uploadVisible" @click="clearUploadFile">
      清空上传列表
    </el-button>
    <transition name="flow">
      <el-upload
        accept="image/*"
        v-show="uploadVisible"
        class="upload-container"
        ref="upload"
        drag
        :multiple="true"
        list-type="picture"
        :action="uploadAction"
        :with-credentials="true"
        :data="uploadData"
        :before-upload="beforeUpload"
        :on-success="successUpload"
        :on-error="errorUpload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处
          <br />
          或
          <br />
          <em>点击上传</em>
          <div class="el-upload__tip" slot="tip">
            只能上传图片文件，且不超过10m
          </div>
        </div>
      </el-upload>
    </transition>
  </div>
</template>

<script>
import serverConfig from '../../../server-config'

export default {
  name: 'MediaUpload',
  props: {
    afterUpload: {
      type: Function,
      default: function () {},
    },
  },
  data: function () {
    return {
      uploadVisible: false,
      uploadAction: serverConfig.api + 'api/admin/media/upload',
      uploadData: {
        path: '',
      },
    }
  },
  methods: {
    clearUploadFile() {
      this.$refs.upload.clearFiles()
    },
    beforeUpload(file) {
      let fileName = file.name

      const size = file.size / (1024 * 1024)
      if (size > 10) {
        this.$util.message.error(fileName + '大于10m')
        return false
      }

      this.uploadData.path = this.$dayjs(new Date()).format('YYYY/MM')
    },
    successUpload(response, file) {
      if (response.success) {
        this.$util.message.success('上传' + file.name + '成功!')
      } else {
        this.$util.message.error('上传' + file.name + '失败!' + response.msg)
      }
      this.afterUpload(response, file)
    },
    errorUpload(err, file) {
      this.$util.message.error('网络异常,上传' + file.name + '失败!')
      console.log(err)
    },
  },
}
</script>

<style>
.el-upload--picture {
  display: block;
}

.el-upload-dragger {
  width: 100%;
}
</style>

<style scoped>
.upload-component {
  margin-bottom: 24px;
}

.upload-container {
  margin-top: 24px;
  text-align: center;
}

.upload-container .el-icon-upload {
  margin-top: 20px;
}

/* flow */
.flow-enter-active,
.flow-leave-active {
  transition: all 0.5s;
}

.flow-enter,
.flow-leave {
  transform: translateY(-20px);
  opacity: 0;
}
</style>
