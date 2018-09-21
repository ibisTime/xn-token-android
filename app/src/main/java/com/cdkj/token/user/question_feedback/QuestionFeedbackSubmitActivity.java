package com.cdkj.token.user.question_feedback;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AddPhotoAdapter;
import com.cdkj.token.databinding.ActivityQuestionFeedbackBinding;
import com.cdkj.token.model.ClientPickerModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import retrofit2.Call;

/**
 * 问题反馈
 * Created by cdkj on 2018/8/4.
 */

public class QuestionFeedbackSubmitActivity extends AbsLoadActivity {

    private ActivityQuestionFeedbackBinding mbinding;

    private PermissionHelper mPreHelper;//权限请求

    private String[] needLocationPermissions; //需要申请的权限
    private final int PHOTOREQUESTCODE = 100;//打开相册


    private AddPhotoAdapter mAddpAdapter;
    private List<String> imgStringList;
    private QiNiuHelper qiNiuHelper;

    private String mFromType = "Android";//来源 默认安卓


    private OptionsPickerView clicetPickerView;

    private List<ClientPickerModel> clientPickerModels;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QuestionFeedbackSubmitActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_question_feedback, null, false);
        return mbinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        QuestionFeedbackHistoryListActivity.open(this);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.question_feedback);
        mBaseBinding.titleView.setRightTitle(getString(R.string.question_history));
        initPermissionHelper();
        initRecyclerView();
        initClickListener();

    }

    private void initClickListener() {

        //选择来源
        mbinding.linLayoutQuestionFrom.setOnClickListener(view -> {
            showClientPickerView();
        });

        //提交
        mbinding.btnSubmit.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mbinding.editQuestion.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.question_input_hint_1));
                return;
            }

            if (TextUtils.isEmpty(mbinding.editReplay.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.question_input_hint_2));
                return;
            }

            if (!mAddpAdapter.getSelectUrlList().isEmpty()) {
                uploadPhotosAndSubmit();
                return;
            }

            submitRequest();
        });

    }

    /**
     * 上传图片并提交
     */
    private void uploadPhotosAndSubmit() {
        if (qiNiuHelper == null) {
            qiNiuHelper = new QiNiuHelper(this);
        }
        imgStringList = new ArrayList<>();
        showLoadingDialog();
        ArrayList<String> selectUrlList = mAddpAdapter.getSelectUrlList();
        qiNiuHelper.upLoadListPic(selectUrlList, new QiNiuHelper.upLoadListImageListener() {
            @Override
            public void onChange(int index, String url) {
                imgStringList.add(url);
            }

            @Override
            public void onSuccess() {
                submitRequest();
            }

            @Override
            public void onFal(String info) {
                disMissLoadingDialog();
                ToastUtil.show(QuestionFeedbackSubmitActivity.this, info);
            }

            @Override
            public void onError(String info) {
                disMissLoadingDialog();
                ToastUtil.show(QuestionFeedbackSubmitActivity.this, info);
            }
        });
    }

    /**
     * 提交请求
     */
    private void submitRequest() {
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();

        map.put("deviceSystem", mFromType);
        map.put("description", mbinding.editQuestion.getText().toString());
        map.put("reappear", mbinding.editReplay.getText().toString());
        map.put("pic", getSelectPicUrls());
        map.put("commitUser", SPUtilHelper.getUserId());
        map.put("commitNote", mbinding.editRemark.getText().toString());

        Call<BaseResponseModel<CodeModel>> call = RetrofitUtils.getBaseAPiService().codeRequest("805100", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {

                if (!TextUtils.isEmpty(data.getCode())) {
                    UITipDialog.showSuccess(QuestionFeedbackSubmitActivity.this, getString(R.string.question_submit_success), dialogInterface -> {
                        QuestionFeedbackHistoryListActivity.open(QuestionFeedbackSubmitActivity.this);
                        finish();
                    });
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }

    /**
     * 获取选择的图片url
     *
     * @return
     */
    private String getSelectPicUrls() {
        return StringUtils.listToString(imgStringList, "||");
    }


    /**
     * 显示客户端选择
     */
    public void showClientPickerView() {

        initClientPickerModels();
        if (clicetPickerView == null) {
            //条件选择器
            clicetPickerView = new OptionsPickerBuilder(QuestionFeedbackSubmitActivity.this, new OnOptionsSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    ClientPickerModel clientPickerModel = clientPickerModels.get(options1);
                    if (clientPickerModel == null) return;
                    mFromType = clientPickerModel.getType();
                    mbinding.tvFrom.setText(clientPickerModel.getName());
                }
            }).setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.sure))//确认按钮文字
                    .build();
        }

        clicetPickerView.setPicker(clientPickerModels);
        clicetPickerView.show();
    }

    /**
     * 初始化选择图片
     */
    void initClientPickerModels() {
        if (clientPickerModels == null) {
            clientPickerModels = new ArrayList<>();

            ClientPickerModel clientPickerModel = new ClientPickerModel();
            clientPickerModel.setName(getString(R.string.android_txt));
            clientPickerModel.setType("Android");

            ClientPickerModel clientPickerModel2 = new ClientPickerModel();
            clientPickerModel2.setName(getString(R.string.ios_text));
            clientPickerModel2.setType("iOS");

            ClientPickerModel clientPickerModel3 = new ClientPickerModel();
            clientPickerModel3.setName(getString(R.string.h5_text));
            clientPickerModel3.setType("H5");

            clientPickerModels.add(clientPickerModel);
            clientPickerModels.add(clientPickerModel2);
            clientPickerModels.add(clientPickerModel3);
        }
    }


    //recyclerView
    private void initRecyclerView() {
        mAddpAdapter = new AddPhotoAdapter();

        mAddpAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.fra_delete:
                    mAddpAdapter.removeItem(position);
                    break;
                case R.id.img:
                    if (!mAddpAdapter.isAddPhoto(position)) {
                        return;
                    }
                    requestPermissions();
                    break;
            }
        });

        mbinding.photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mbinding.photoRecyclerView.setNestedScrollingEnabled(false);
        mbinding.photoRecyclerView.setAdapter(mAddpAdapter);
    }

    /**
     * 初始化权限请求
     */
    private void initPermissionHelper() {
        needLocationPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        mPreHelper = new PermissionHelper(this);
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {

        mPreHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                choicePhotoWrapper();
            }

            @Override
            public void doAfterDenied(String... permission) {
                UITipDialog.showFail(QuestionFeedbackSubmitActivity.this, getString(R.string.activity_image_system_content));
            }
        }, needLocationPermissions);
    }

    /**
     * 打开相册选择
     */
    private void choicePhotoWrapper() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = null;
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) { //判断sd卡是否存在
            takePhotoDir = new File(Environment.getExternalStorageDirectory(), AppConfig.CACHDIR);
        }
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir)
                .maxChooseCount(AddPhotoAdapter.PHOTOCOUNT) // 图片选择张数的最大值
                .selectedPhotos(mAddpAdapter.getSelectUrlList()) // 当前已选中的图片路径集合
                .pauseOnScroll(true) // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, PHOTOREQUESTCODE);
    }

    //图片选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PHOTOREQUESTCODE) {
            ArrayList<String> mSelectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
            mAddpAdapter.updateUrlList(mSelectedPhotos);
        }
    }

    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPreHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (qiNiuHelper != null) {
            qiNiuHelper.clear();
        }
        super.onDestroy();
    }
}
