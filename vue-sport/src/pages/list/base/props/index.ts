import { Avatar, FormRule, Link, PrimaryTableCol, Space, Tag } from 'tdesign-vue-next';
import { t } from '@/locales';

const searchParams: SearchParamsProps[] = [
    {
        label: '昵称',
        key: 'nickname',
    },
    {
        label: '性别',
        key: 'sex',
        valueType: 'select',
        defaultValue: 2,
        options: [
            {
                label: '男',
                value: 0,
            },
            {
                label: '女',
                value: 1,
            },
            {
                label: '未知',
                value: 2,
            }
        ]
    }
]

const COLUMNS: PrimaryTableCol[] = [
    {
        title: '用户名',
        fixed: 'left',
        ellipsis: true,
        align: 'center',
        colKey: 'username',
    },
    {
        title: '头像',
        ellipsis: true,
        colKey: 'avatar',
        align: 'center',
        cell: (h, props) => h(Avatar, { image: props.row.avatar }),
    },
    {
        title: '性别',
        colKey: 'sex',
        width: 160,
        align: 'center',
        cell: (h, { row }) => h(Tag, {
            theme: 'danger',
            variant: 'light',
        }, row.sex == 0 ? '男' : row.sex == 1 ? '女' : '未知'),
    },
    {
        title: t('pages.userList.nickname'),
        ellipsis: true,
        colKey: 'nickname',
        align: 'center',
    },
    {
        title: '电话',
        ellipsis: true,
        colKey: 'phone',
        align: 'center',
    },
    {
        title: '邮件',
        ellipsis: true,
        colKey: 'email',
        align: 'center',
    },
];

// 属性校验
const FORM_RULES: Record<string, FormRule[]> = {
    phone: [{ required: true, message: t('pages.login.required.phone'), type: 'error', trigger: 'blur'}],
    username: [{ required: true, message: t('pages.login.required.account'), type: 'error', trigger: 'blur' }],
    password: [{ required: true, message: t('pages.login.required.password'), type: 'error', trigger: 'blur' }],
    email: [{ email: true, message: "邮箱地址不正确" }],
  };
export { searchParams, COLUMNS, FORM_RULES };