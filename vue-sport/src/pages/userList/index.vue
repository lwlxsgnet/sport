<template>
    <div class="list-common-table">
        <t-form ref="form" :data="formData" :label-width="80" colon @reset="onReset" @submit="onSubmit">
            <t-row>
                <t-col :span="10">
                    <t-row :gutter="[24, 24]">
                        <t-col :span="4">
                            <t-form-item :label="t('pages.userList.nickname')" name="nickname">
                                <t-input v-model="formData.nickname" class="form-item-content" type="search"
                                    :placeholder="t('pages.userList.nicknamePlaceHolder')"
                                    :style="{ minWidth: '134px' }" />
                            </t-form-item>
                        </t-col>
                    </t-row>
                </t-col>

                <t-col :span="2" class="operation-container">
                    <t-button theme="primary" type="submit" :style="{ marginLeft: 'var(--td-comp-margin-s)' }">
                        {{ t('components.commonTable.query') }}
                    </t-button>
                    <t-button type="reset" variant="base" theme="default"> {{ t('components.commonTable.reset') }}
                    </t-button>
                </t-col>
            </t-row>
        </t-form>
        <t-row style="margin-top: 10px;">
            <t-col :span="8">
                <t-button @click="insert">新增用户</t-button>
            </t-col>
        </t-row>

        <div class="table-container">
            <t-table :data="data" :columns="COLUMNS" :row-key="rowKey" :vertical-align="verticalAlign" :hover="hover"
                :pagination="pagination" :loading="dataLoading" :header-affixed-top="headerAffixedTop"
                @page-change="pageChange"> <!-- @change="rehandleChange" -->
                <template #sex="{ row }">
                    <t-tag v-if="row.sex == 0" theme="danger" variant="light">
                        男
                    </t-tag>
                    <t-tag v-else-if="row.sex == 1" theme="danger" variant="light">
                        女
                    </t-tag>
                    <t-tag v-else theme="danger" variant="light">
                        未知
                    </t-tag>
                </template>
                <template #avatar="{ row }">
                    <t-avatar :image="row.avatar" />
                </template>
                <template #op="slotProps">
                    <t-space>
                        <t-link theme="primary" @click="handleClickDetail()"> {{ t('pages.listBase.detail') }}</t-link>
                        <t-link theme="danger" @click="handleClickDelete(slotProps)"> {{ t('pages.listBase.delete')
                            }}</t-link>
                    </t-space>
                </template>
            </t-table>
            <t-dialog v-model:visible="confirmVisible" header="确认删除当前所选用户？" :body="confirmBody" :on-cancel="onCancel"
                @confirm="onConfirmDelete" />

            <t-dialog header="请填写用户信息" v-model:visible="modelOpen" >
                <!-- ref 不能和data定义名称一样 -->
                <t-form ref="submitFormRef" :data="submitForm" colon @submit="onModelSubmit" :rules="FORM_RULES">
                    <t-form-item label="用户名" name="username">
                        <t-input v-model="submitForm.username" type="search" placeholder="请输入用户名" />
                    </t-form-item>
                    <t-form-item label="密码" name="password">
                        <t-input type="password" v-model="submitForm.password" placeholder="请输入密码" >
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
                    <t-form-item label="电话" name="phone">
                        <t-input-number theme="normal" v-model="submitForm.phone" placeholder="请输入电话" 
                            style="width: 100%"/>
                    </t-form-item>
                    <t-form-item label="邮件" name="email">
                        <t-input v-model="submitForm.email" placeholder="请输入邮件" />
                    </t-form-item>
                    <t-form-item label="头像" name="avatar">
                    </t-form-item>
                </t-form>
            </t-dialog>
        </div>
    </div>
</template>
<script setup lang="ts">
import { MessagePlugin, PageInfo, PrimaryTableCol, TableRowData } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import { getList } from '@/api/list';
import Trend from '@/components/trend/index.vue';
import { prefix } from '@/config/global';
import { CONTRACT_PAYMENT_TYPES, CONTRACT_STATUS, CONTRACT_TYPES } from '@/constants';
import { t } from '@/locales';
import { useSettingStore } from '@/store';
import { page } from '@/api/sysUser'
import { reactive } from 'vue';
import type { FormInstanceFunctions, FormRule, SubmitContext } from 'tdesign-vue-next';


interface FormData {
    nickname: string;
    status?: number;
}

const store = useSettingStore();
const router = useRouter();

const CONTRACT_STATUS_OPTIONS = [
    { value: CONTRACT_STATUS.FAIL, label: t('components.commonTable.contractStatusEnum.fail') },
    { value: CONTRACT_STATUS.AUDIT_PENDING, label: t('components.commonTable.contractStatusEnum.audit') },
    { value: CONTRACT_STATUS.EXEC_PENDING, label: t('components.commonTable.contractStatusEnum.pending') },
    { value: CONTRACT_STATUS.EXECUTING, label: t('components.commonTable.contractStatusEnum.executing') },
    { value: CONTRACT_STATUS.FINISH, label: t('components.commonTable.contractStatusEnum.finish') },
];

const CONTRACT_TYPE_OPTIONS = [
    { value: CONTRACT_TYPES.MAIN, label: t('components.commonTable.contractTypeEnum.main') },
    { value: CONTRACT_TYPES.SUB, label: t('components.commonTable.contractTypeEnum.sub') },
    { value: CONTRACT_TYPES.SUPPLEMENT, label: t('components.commonTable.contractTypeEnum.supplement') },
];
const COLUMNS: PrimaryTableCol[] = [
    {
        title: '用户名',
        fixed: 'left',
        width: 280,
        ellipsis: true,
        align: 'left',
        colKey: 'username',
    },
    {
        title: '头像',
        width: 160,
        ellipsis: true,
        colKey: 'avatar',
    },
    {
        title: '性别',
        colKey: 'sex',
        width: 160
    },
    {
        title: t('pages.userList.nickname'),
        width: 160,
        ellipsis: true,
        colKey: 'nickname',
    },
    {
        title: '电话',
        width: 160,
        ellipsis: true,
        colKey: 'phone',
    },
    {
        title: '邮件',
        width: 160,
        ellipsis: true,
        colKey: 'email',
    },
    {
        align: 'left',
        fixed: 'right',
        width: 160,
        colKey: 'op',
        title: t('components.commonTable.operation'),
    },
];

const searchForm = {
    nickname: '',
};

const formData = ref<FormData>(searchForm);
const rowKey = 'index';
const verticalAlign = 'top' as const;
const hover = true;

const pagination = ref({
    defaultPageSize: 10,
    total: 0,
    defaultCurrent: 1,
});
const confirmVisible = ref(false);
const modelOpen = ref(false);
const INITIAL_DATA = {
  phone: '',
  username: '',
  password: '',
  sex: 2,
  email: '',
};
const submitForm = ref(INITIAL_DATA);

const FORM_RULES: Record<string, FormRule[]> = {
  phone: [{ required: true, message: t('pages.login.required.phone'), type: 'error' }],
  username: [{ required: true, message: t('pages.login.required.account'), type: 'error' }],
  password: [{ required: true, message: t('pages.login.required.password'), type: 'error' }],
  email: [{ email: true, message: "邮箱地址不正确" }],
};

const data = ref([]);

const dataLoading = ref(false);

/**
 * 表格数据查询
 * @param pageNo 
 * @param pageSize 
 */
const findData = async (pageNo: number, pageSize: number) => {
    dataLoading.value = true;
    try {
        const { total, records } = await page({ pageNo, pageSize, params: formData.value });
        data.value = records;
        pagination.value = {
            ...pagination.value,
            total: total,
        };
    } catch (e) {
        console.log(e);
    } finally {
        dataLoading.value = false;
    }
};

const deleteIdx = ref(-1);
const confirmBody = computed(() => {
    if (deleteIdx.value > -1) {
        const { name } = data.value[deleteIdx.value];
        return `删除后，${name}的所有合同信息将被清空，且无法恢复`;
    }
    return '';
});

const resetIdx = () => {
    deleteIdx.value = -1;
};

const onConfirmDelete = () => {
    // 真实业务请发起请求
    data.value.splice(deleteIdx.value, 1);
    pagination.value.total = data.value.length;
    confirmVisible.value = false;
    MessagePlugin.success('删除成功');
    resetIdx();
};

const onCancel = () => {
    resetIdx();
};

/**
 * 页面挂载时，获取数据 dom渲染完成
 */
onMounted(() => {
    findData(pagination.value.defaultCurrent, pagination.value.defaultPageSize);
});

const handleClickDelete = (slot: { row: { rowIndex: number } }) => {
    deleteIdx.value = slot.row.rowIndex;
    confirmVisible.value = true;
};
const onReset = (val: unknown) => {
    console.log(val);
};

const handleClickDetail = () => {
    router.push('/detail/base');
};

//查询按钮点击
const onSubmit = (val: unknown) => {
    findData(pagination.value.defaultCurrent, pagination.value.defaultPageSize);
    //console.log(val);
    //console.log(formData.value);
};

//页面变化，查询数据
const pageChange = ({ current, pageSize }: PageInfo) => {
    findData(current, pageSize);
    //console.log('分页变化', pageInfo);
};
// const rehandleChange = (changeParams: unknown, triggerAndData: unknown) => {
//     console.log('统一Change', changeParams, triggerAndData);
// };

const onModelSubmit = (cxt: SubmitContext) => {
    if(cxt.validateResult) {  }
}

const insert = () => {
    //打开dialog
    modelOpen.value = true;
    submitForm.value = {
        username: '',
        password: '',
        sex: 2,
        phone: '',
        email: '',
    }
}

const headerAffixedTop = computed(
    () =>
        ({
            offsetTop: store.isUseTabsRouter ? 48 : 0,
            container: `.${prefix}-layout`,
        }) as any, // TO BE FIXED
);
</script>

<style lang="less" scoped>
.list-common-table {
    background-color: var(--td-bg-color-container);
    padding: var(--td-comp-paddingTB-xxl) var(--td-comp-paddingLR-xxl);
    border-radius: var(--td-radius-medium);

    .table-container {
        margin-top: var(--td-comp-margin-xxl);
    }
}

.form-item-content {
    width: 100%;
}

.operation-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .expand {
        .t-button__text {
            display: flex;
            align-items: center;
        }
    }
}

.payment-col {
    display: flex;

    .trend-container {
        display: flex;
        align-items: center;
        margin-left: var(--td-comp-margin-s);
    }
}
</style>

<script lang="ts">
export default {
    name: 'ListFilter',
};
</script>