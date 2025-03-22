<template>
    <div class="list-common-table">
        <!-- search section -->
        <t-form ref="form" :data="formData" :label-width="80" colon @reset="onReset" @submit="onSearch"
            v-if="searchParams && searchParams.length > 0">
            <t-row>
                <t-col :span="10">
                    <t-row :gutter="[24, 24]">
                        <t-col :span="4" v-for="(item, index) in searchParams" :key="index">
                            <t-form-item :label="item.label as string" :name="item.key">
                                <t-input v-if="item.valueType != 'select'" v-model="formData[`${item.key}`]"
                                    :value="item?.defaultValue" clearable />
                                <t-select v-else v-model="formData[`${item.key}`]" :options="item.options" clearable />
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
        <t-row style="margin: 20px 10px;">
            <slot name="tableHeader"></slot>
        </t-row>
        <div class="table-container">
            <t-table :data="data" :columns="columns" :row-key="rowKey" :pagination="pagination" 
                :loading="dataLoading"
                @page-change="pageChange" /> <!-- @change="rehandleChange" -->
        </div>
    </div>
</template>

<script setup lang="ts">
import { FormInstanceFunctions, PageInfo, PaginationProps } from 'tdesign-vue-next';
import { onMounted, PropType, ref } from 'vue';
import { t } from '@/locales';

const form = ref<FormInstanceFunctions>(null);
const formData = ref<Record<string, any>>({});
const data = ref([]);
const dataLoading = ref(false);

/**
 * 子组件向父组件
 */
const props = defineProps({
    searchParams: {
        type: Array as PropType<SearchParamsProps[]>,
    },
    columns: {
        type: Array
    },
    rowKey: {
        type: String,
        default: 'index',
    },
    pageSizes: {
        type: Array as PropType<number[]>,
        default: [5, 10, 20, 30, 50, 100], 
    },
    asyncFn: {
        type: Function,
    }
});

/**
 * 组件初始进来，判断是否存在表单项默认值
 */
if (props.searchParams && props.searchParams.length > 0) {
    const data = {} as Record<string, any>;
    //迭代表单项
    props.searchParams.forEach(item => {
        // 如果存在默认值 
        if (item.defaultValue) {
            // 将表单项 即v-model 赋值
            data[item.key] = item.defaultValue;
        }
    });
    formData.value = data;
}
/**
 * request query
 * @param pageNo 
 * @param pageSize 
 */
const findData = async (pageNo: number, pageSize: number) => {
    dataLoading.value = true;
    try {
        const { total, records } = await props.asyncFn({pageNo, pageSize, params: formData.value});
        data.value = records;
        pagination.value = {
            defaultPageSize: pageSize,
            defaultCurrent: pageNo,
            total: total,
        };
    } catch (e) {
        console.log(e);
    } finally {
        dataLoading.value = false;
    }
}

// 分页
const pagination = ref<PaginationProps>({
    defaultPageSize: 10,
    total: 0,
    defaultCurrent: 1,
    pageSizeOptions: props.pageSizes
})

//form reset
const onReset = () => {
    form.value.reset();
    //处理v-model和:value冲突
    formData.value = {
        sex: 2,
    };
    findData(pagination.value.defaultCurrent, pagination.value.defaultPageSize);
};

//form submit
const onSearch = () => {
    findData(pagination.value.defaultCurrent, pagination.value.defaultPageSize);
    //console.log(formData.value);
};

/**
 * form changed, execute query
 * @param param
 */
const pageChange = ({ current, pageSize }: PageInfo) => {
    findData(current, pageSize);
}

//page loading
onMounted(() => {
    findData(pagination.value.defaultCurrent, pagination.value.defaultPageSize);
})

/**
 * 子组件向父组件暴露方法
 */
defineExpose({ onSearch })
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