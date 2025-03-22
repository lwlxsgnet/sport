<template>
  <div>
    <s-table :search-params="searchParams" :columns="columns" :async-fn="page" ref="table">
      <template #tableHeader>
        <t-button @click="insert">新增用户</t-button>
      </template>
    </s-table>
    <t-dialog header="请填写用户信息" v-model:visible="modelOpen" @confirm="onModelSubmit">
      <!-- ref 不能和data定义名称一样 -->
      <t-form ref="submitFormRef" :data="submitForm" colon :rules="FORM_RULES">
        <t-form-item label="用户名" name="username">
          <t-input v-model="submitForm.username" type="search" placeholder="请输入用户名" />
        </t-form-item>
        <t-form-item label="密码" name="password" v-if="!submitForm.id">
          <t-input type="password" v-model="submitForm.password" placeholder="请输入密码">
            <template #prefix-icon>
              <lock-on-icon />
            </template>
          </t-input>
        </t-form-item>
        <t-form-item label="性别" name="sex">
          <t-select placeholder="请选择性别" v-model="submitForm.sex">
            <t-option label="男" :value="0" />
            <t-option label="女" :value="1" />
            <t-option label="未知" :value="2" />
          </t-select>
        </t-form-item>
        <t-form-item label="昵称" name="nickname">
          <t-input v-model="submitForm.nickname" placeholder="请输入昵称" />
        </t-form-item>
        <t-form-item label="电话" name="phone">
          <t-input-number theme="normal" v-model="submitForm.phone" placeholder="请输入电话" style="width: 100%" />
        </t-form-item>
        <t-form-item label="邮件" name="email">
          <t-input v-model="submitForm.email" placeholder="请输入邮件" />
        </t-form-item>
        <t-form-item label="头像" name="avatar">
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>


<script setup lang="ts">
import STable from '@/components/s-table/index.vue';
import { page, update, insert as insertApi, deleteById } from '@/api/sysUser';
import { COLUMNS, searchParams, FORM_RULES } from '@/pages/list/base/props/index';
import { ref } from 'vue';
import { t } from '@/locales'
import { DialogPlugin, FormInstanceFunctions, Link, MessagePlugin, Space } from 'tdesign-vue-next';

const modelOpen = ref(false);
const submitFormRef = ref<FormInstanceFunctions>(null);
const submitForm = ref({});

const table = ref<TableInstanceFunctions>(null);

const columns = COLUMNS.concat([
  {
    align: 'center',
    fixed: 'right',
    colKey: 'op',
    title: t('components.commonTable.operation'),
    cell: (h, { row }) => h(Space, {}, [
      h(Link, {
        theme: 'primary',
        onClick: () => {
          // 表单赋值
          submitForm.value = row;
          // 打开dialog
          modelOpen.value = true;
          console.log('点击了修改', row)
        }
      }, '修改'),
      h(Link, {
        theme: 'danger',
        onClick: () => {
          const confirmDialog = DialogPlugin.confirm({
            header: '提示',
            body: `删除后，标记为${row.username}的用户信息将被清空，且无法恢复`,
            confirmBtn: {
              content: '提交',
              theme: 'primary',
              loading: false,
            },
            theme: 'warning',
            onConfirm: async () => {
              confirmDialog.update({ confirmBtn: { content: '提交', loading: true }});
              await deleteById(row.id);
              confirmDialog.update({ confirmBtn: { content: '提交中', loading: false }});
              MessagePlugin.success('操作成功');
              confirmDialog.hide();
              table.value.onSearch();
            },
          })
        }
      }, '删除'),
    ]),
  },
]);


//新增用户
const insert = () => {
  //打开dialog
  modelOpen.value = true;
  //重置表单
  submitForm.value = {
    sex: 2,
  }
}

/**
 * 提交表单
 * @param  
 */
const onModelSubmit = async () => {
  const valid = await submitFormRef.value.validate();
  if (valid) {
    if (submitForm.value.id) {
      //更新
      await update(submitForm.value);
    } else {
      //新增
      await insertApi(submitForm.value);
    }
    MessagePlugin.success('操作成功');
    modelOpen.value = false;
    //刷新数据
    table.value.onSearch();
  }
}
</script>

<style lang="less" scoped></style>
