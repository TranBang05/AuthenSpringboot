package com.example.demo_mhdigital.data.constant;

public class MessageResponse {
    private MessageResponse() {
    }
    public static final String USERNAME_NOT_FOUND = "Tài khoản %s không tồn tại!";
    public static final String ID_MUST_NOT_BE_NULL = "ID không được để trống!";
    public static final String SERVER_ERROR = "Có lỗi hệ thống. Vui lòng tải lại trang hoặc liên hệ quản trị viên.";

    public static final String GRAPH_FACEBOOK_PROBLEM = "Máy chủ Graph Facebook đang bận";
    public static final String INVALID_AUTHENTICATION_INFO = "Tài khoản hoặc mật khẩu không đúng.";
    public static final String GRAPH_GOOGLE_PROBLEM = "Máy chủ Google đang bận";

    //Đăng ký tài khoản
    public static final String REGISTER_DUPLICATE_EMAIL = "Email đã được dùng cho tài khoản khác!";
    public static final String REGISTER_DUPLICATE_PHONE = "Số điện thoại đã được dùng cho tài khoản khác!";
    public static final String SUCCESS = "success";

    public static final String HAVE_APPLICANT ="[HOT] Có ứng viên mới cho vị trí ";
    public static final String HAVE_PARTNER ="[HOT] Có đối tác quan tâm tới vấn đề ";
    public static final String THANKS_APPLICANT ="[MH Sloution] Thư xác nhận thông tin ứng tuyển";
}
