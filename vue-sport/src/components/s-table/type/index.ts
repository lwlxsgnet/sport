interface SearchParamsProps{
    /**
     * 表单项名称
     */
    label: string | import("vue").PropType<string | ((h: typeof import("vue").h) => import("..").SlotReturnValue)>;
    /**
     * 搜索属性
     */
    key: string;
    /**
     * 下拉搜索和文字搜索
     */
    valueType?: 'select' | 'text';
    /**
     * 表单项默认值
     */
    defaultValue?: string | number;
    /**
     * 如果是select, 则必填
     */
    options?: {label: string; value: string | number; }[];
}

interface TableInstanceFunctions {
/**
 * 主动刷新表单
 */
    onSearch: () => void;

}
