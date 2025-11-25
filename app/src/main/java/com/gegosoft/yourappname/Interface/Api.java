package com.gegosoft.yourappname.Interface;

import com.gegosoft.yourappname.Models.AddMarkStudentListModel;
import com.gegosoft.yourappname.Models.EditRoomModel;
import com.gegosoft.yourappname.Models.ExamListModel;
import com.gegosoft.yourappname.Models.InvitesModel;
import com.gegosoft.yourappname.Models.JoinRoomModel;
import com.gegosoft.yourappname.Models.RoomListModel;
import com.gegosoft.yourappname.Models.StandardList;
import com.gegosoft.yourappname.Models.StudentList;
import com.gegosoft.yourappname.Models.SuccessApiCallModel;
import com.gegosoft.yourappname.Models.VideoRoomSubjectModel;
import com.gegosoft.yourappname.Models.ViewMarkModel;
import com.gegosoft.yourappname.Models.AbsentlisModel;
import com.gegosoft.yourappname.Models.AddAssignmentModel;
import com.gegosoft.yourappname.Models.AddAttendanceAbsentiesmodel;
import com.gegosoft.yourappname.Models.AddHomeworklistModel;
import com.gegosoft.yourappname.Models.AddLeaveModel;
import com.gegosoft.yourappname.Models.Addcomments;
import com.gegosoft.yourappname.Models.AddonModel;
import com.gegosoft.yourappname.Models.AllNotificationModel;
import com.gegosoft.yourappname.Models.ApiSuccessModel;
import com.gegosoft.yourappname.Models.ApproveLeaveModel;
import com.gegosoft.yourappname.Models.AssignmentListModel;
import com.gegosoft.yourappname.Models.AttendanceAbsentiesModel;
import com.gegosoft.yourappname.Models.AttendanceModel;
import com.gegosoft.yourappname.Models.ClassNoticeBoardModel;
import com.gegosoft.yourappname.Models.CompletedHomeworkModel;
import com.gegosoft.yourappname.Models.CompletedStudentleaveModel;
import com.gegosoft.yourappname.Models.DeleteAssignmentModel;
import com.gegosoft.yourappname.Models.DeleteLeaveModel;
import com.gegosoft.yourappname.Models.EditAssignmentModel;
import com.gegosoft.yourappname.Models.EditLeaveModel;
import com.gegosoft.yourappname.Models.HolidayListModel;
import com.gegosoft.yourappname.Models.LeaveTypeModel;
import com.gegosoft.yourappname.Models.LessonPlanModel;
import com.gegosoft.yourappname.Models.LogOutModel;
import com.gegosoft.yourappname.Models.LoginModel;
import com.gegosoft.yourappname.Models.MessageModel;
import com.gegosoft.yourappname.Models.MyLeaveModel;
import com.gegosoft.yourappname.Models.MyProfileModel;
import com.gegosoft.yourappname.Models.PendingHomeworkModel;
import com.gegosoft.yourappname.Models.PendingStudentleaveModel;
import com.gegosoft.yourappname.Models.RejectLeaveModel;
import com.gegosoft.yourappname.Models.RequestedLeaveListModel;
import com.gegosoft.yourappname.Models.ShowAssignmentModel;
import com.gegosoft.yourappname.Models.ShowHomeworklistModel;
import com.gegosoft.yourappname.Models.ShowLeaveModel;
import com.gegosoft.yourappname.Models.ShowLessonPlanModel;
import com.gegosoft.yourappname.Models.StandardLinkListModel;
import com.gegosoft.yourappname.Models.StudentCompletedAssignmentModel;
import com.gegosoft.yourappname.Models.StudentListModel;
import com.gegosoft.yourappname.Models.StudentSubmittedAssignmentModel;
import com.gegosoft.yourappname.Models.StudentSubmittedHomeworkModel;
import com.gegosoft.yourappname.Models.SubjectListModel;
import com.gegosoft.yourappname.Models.TeacherListModel;
import com.gegosoft.yourappname.Models.Teacher_School_NoticeboardModel;
import com.gegosoft.yourappname.Models.ToDolistAdd_SpinnerModel;
import com.gegosoft.yourappname.Models.UpdateFCMTokenModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("api/teacher/login")
    Call<LoginModel> login(
            @Field("email")String email,
            @Field("password")String password,
            @Field("device_name")String platform_token,
            @Field("device_id")String device_id);

    @GET("api/v2/addons")
    Call<AddonModel> getAddons(@HeaderMap Map<String,String> headerMap);

    @GET("api/v2/updatetoken")
    Call<UpdateFCMTokenModel> updatefcmtoken(@HeaderMap Map<String,String> headerMap, @Query("device_name") String token);

    @GET("api/teacher/leave/add/list")
    Call<LeaveTypeModel> getleavetype(@HeaderMap Map<String,String> stringStringMap);

    @FormUrlEncoded
    @POST("api/teacher/leave/add")
    Call<AddLeaveModel> addleavemodel(@HeaderMap Map<String,String> stringStringMap,
                                      @Field("from_date") String from_date,
                                      @Field("to_date") String to_date,
                                      @Field("reason_id")int reason_id,
                                      @Field("remarks") String remarks,
                                      @Field("session") String session,
                                      @Field("leave_type_id") int leave_type_id);

    @GET("api/teacher/myleaves")
    Call<MyLeaveModel> getmyleave(@HeaderMap Map<String,String> stringStringMap);

    @Multipart
    @POST("api/teacher/assignment/add")
    Call<AddAssignmentModel>getaddassignment(@HeaderMap Map<String,String>stringStringMap,
                                             @Part("standardLink_id") RequestBody standardLink_id,
                                             @Part("subject_id") RequestBody subject_id,
                                             @Part("title") RequestBody title,
                                             @Part("description") RequestBody description,
                                             @Part("marks") RequestBody marks,
                                             @Part("assigned_date") RequestBody assigned_date,
                                             @Part("submission_date") RequestBody submission_date,
                                             @Part MultipartBody.Part file);



    @GET("api/teacher/assignment/standardLinklist")
    Call<StandardLinkListModel> standardlinklist(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/assignment/subjectlist/{standardid}")
    Call<SubjectListModel> subjectlist(@HeaderMap Map<String,String> stringStringMap, @Path("standardid") String standardid);

    @GET("api/teacher/assignments")
    Call<AssignmentListModel> getassignmentlist(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/assignment/delete/{assignmentid}")
    Call<DeleteAssignmentModel> getdeleteassignmt(@HeaderMap Map<String,String> stringStringMap, @Path("assignmentid") String assignmentid);

    @FormUrlEncoded
    @POST("api/teacher/assignment/edit/{assignmentid}")
    Call<EditAssignmentModel> geteditassignmentmodel(@HeaderMap Map<String,String>stringStringMap,
                                                     @Field("standardLink_id") String standardLink_id,
                                                     @Field("subject_id") String subject_id,
                                                     @Field("title") String title,
                                                     @Field("description") String description,
                                                     @Field("marks") String marks,
                                                     @Field("assigned_date") String assigned_date,
                                                     @Field("submission_date") String submission_date,
                                                     @Path("assignmentid") String assignment);



    @GET("api/teacher/assignment/show/{assignmentid}")
    Call<ShowAssignmentModel> getassignmentmodel(@HeaderMap Map<String,String> stringStringMap, @Path("assignmentid") String assignmentid);

    @GET("api/teacher/leave/show/{leaveid}")
    Call<ShowLeaveModel> getshowleave(@HeaderMap Map<String,String> stringStringMap, @Path("leaveid") String leaveid);

    @FormUrlEncoded
    @POST("api/teacher/leave/edit/{leaveid}")
    Call<EditLeaveModel> geteditleavemodel(@HeaderMap Map<String,String> stringStringMap,
                                           @Path("leaveid") String leaveid,
                                           @Field("from_date") String from_date,
                                           @Field("to_date") String to_date,
                                           @Field("reason_id")int reason_id,
                                           @Field("remarks") String remarks,
                                           @Field("session") String session,
                                           @Field("leave_type_id") int leave_type_id
    );


    @GET("api/teacher/leave/delete/{leaveid}")
    Call<DeleteLeaveModel> getdeleteleave(@HeaderMap Map<String,String> stringStringMap, @Path("leaveid") String leaveid);

    @FormUrlEncoded
    @POST("api/teacher/checkleave/approve/{leaveid}")
    Call<ApproveLeaveModel> getapproveleave(@HeaderMap Map<String,String> stringStringMap, @Path("leaveid") String leaveid,
                                            @Field("comments") String comments);

    @FormUrlEncoded
    @POST("api/teacher/checkleave/reject/{leaveid}")
    Call<RejectLeaveModel> getrejectleave(@HeaderMap Map<String,String> stringStringMap, @Path("leaveid") String leaveid,
                                          @Field("comments") String comments);

    @GET("api/teacher/checkleave/list")
    Call<RequestedLeaveListModel> getrequestedleave(@HeaderMap Map<String,String> stringStringMap);

    @POST("api/v2/logout")
    Call<LogOutModel> getlogout(@HeaderMap Map<String,String> stringStringMap);


    @GET("api/teacher/assignment/show/{assignment_id}/submitted")
    Call<StudentSubmittedAssignmentModel> getstudentssubmittedassignment(@HeaderMap Map<String,String> stringStringMap,
                                                                         @Path("assignment_id") String assignment_id);

    @GET("api/teacher/assignment/show/{assignment_id}/completed")
    Call<StudentCompletedAssignmentModel> getstudentscompletedassignment(@HeaderMap Map<String,String> stringStringMap,
                                                                         @Path("assignment_id") String assignment_id);

    @GET("api/teacher/myinfo")
    Call<MyProfileModel> getprofilemodel(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/holidays")
    Call<HolidayListModel> getholidaymodel(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/lessonplans")
    Call<LessonPlanModel> getlessonplanmodel(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/lessonplan/print/{lessonplan_id}")
    Call<ShowLessonPlanModel> getshowlessonplanmodel(@HeaderMap Map<String,String> stringStringMap, @Path("lessonplan_id") int lessonplan_id);

    @GET("api/teacher/attendance/list")
    Call<AttendanceModel> getattendancemodel(@HeaderMap Map<String, String> headerMap);

    @POST("api/teacher/attendance/add")
    Call<AddAttendanceAbsentiesmodel> getaddattendance(@HeaderMap Map<String,String> stringStringMap,
                                                       @Body AttendanceAbsentiesModel attendanceAbsentiesModel);

    @FormUrlEncoded
    @POST("api/teacher/assignment/addMarks/{id}")
    Call<ApiSuccessModel> getaddmarks(@HeaderMap Map<String,String> stringStringMap,
                                      @Path("id") int id,
                                      @Field("obtained_marks") String obtained_marks,
                                      @Field("comments") String comments);

    @GET("api/teacher/notices/class/{teacher_id}")
    Call<ClassNoticeBoardModel> getClassNotice(@HeaderMap Map<String,String> stringStringMap, @Path("teacher_id") String student_id);

    @GET("api/teacher/my-school/notices")
    Call<Teacher_School_NoticeboardModel> getSchoolNotice(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/messages")
    Call<MessageModel> getMessageList(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/notifications/{teacher_id}")
    Call<AllNotificationModel> getallnotification(@HeaderMap Map<String, String> headermap, @Path("teacher_id") String teacher_id);

    @GET("api/teacher/homeworks/pending")
    Call<PendingHomeworkModel> getpendinghomework(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/homeworks/completed")
    Call<CompletedHomeworkModel> getcompletedhomework(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/homework/show/{student_homework_id}")
    Call<StudentSubmittedHomeworkModel> getsubmittedhomework(@HeaderMap Map<String, String> headermap, @Path("student_homework_id") String student_homework_id);

    @FormUrlEncoded
    @POST("api/teacher/student/homework/edit/{student_homework_id}")
    Call<Addcomments> getaddcomments(@HeaderMap Map<String,String> stringStringMap, @Path("student_homework_id") String student_homework_id,
                                     @Field("comments") String comments);

    @Multipart
    @POST("api/teacher/homework/add")
    Call<AddAssignmentModel>gethomework(@HeaderMap Map<String,String>stringStringMap,
                                        @Part("standardLink_id") RequestBody standardLink_id,
                                        @Part("subject_id") RequestBody subject_id,
                                        @Part("description") RequestBody description,
                                        @Part("date") RequestBody assigned_date,
                                        @Part("submission_date") RequestBody submission_date,
                                        @Part MultipartBody.Part file);



    @Multipart
    @POST("api/teacher/homework/edit/{id}")
    Call<AddHomeworklistModel> getedit(@HeaderMap Map<String,String>stringStringMap,
                                       @Part("standardLink_id") RequestBody standardLink_id,
                                       @Part("subject_id") RequestBody subject_id,
                                       @Part("description") RequestBody description,
                                       @Part("date") RequestBody assigned_date,
                                       @Path("id") String homeworkid,
                                       @Part MultipartBody.Part file);

    @GET("api/teacher/attendance/absentList/{standardLink_id}/{date}/{session}")
    Call<AbsentlisModel> getabsentlist(@HeaderMap Map<String, String> headermap,
                                       @Path("standardLink_id") String standardLink_id,
                                       @Path("date") String date,
                                       @Path("session") String session);

    @GET("api/teacher/homeworks/pendingApproval")
    Call<AddHomeworklistModel>getaddhomeworklist(@HeaderMap Map<String,String> headermap);

    @GET("api/teacher/homework/delete/{homework_id}")
    Call<ApiSuccessModel>getdeletehomeworklist(@HeaderMap Map<String,String> headermap,
                                               @Path("homework_id") String homework_id);
    @GET("api/teacher/homework/edit/{homework_id}")
    Call<ShowHomeworklistModel> getshowhomework(@HeaderMap Map<String,String> stringStringMap, @Path("homework_id") String homework_id);

    @GET("api/teacher/student/leave/pending")
    Call<PendingStudentleaveModel> getpendingleave(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/student/leave")
    Call<CompletedStudentleaveModel> getcompletedleave(@HeaderMap Map<String, String> headermap);


    @FormUrlEncoded
    @POST("api/teacher/student/leave/approve/{leave_id}")
    Call<Addcomments> getleaveapprovecomments(@HeaderMap Map<String,String> stringStringMap, @Path("leave_id") String leave_id,
                                              @Field("comments") String comments);

    @FormUrlEncoded
    @POST("api/teacher/student/leave/reject/{leave_id}")
    Call<Addcomments> getleaverejectcomments(@HeaderMap Map<String,String> stringStringMap, @Path("leave_id") String leave_id,
                                             @Field("comments") String comments);

    @GET("api/teacher/task/add/list")
    Call<ToDolistAdd_SpinnerModel> gettodospinners(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/task/add/student/{standardlink_id}")
    Call<StudentListModel> getstudentslist(@HeaderMap Map<String, String> headermap, @Path("standardlink_id") String standardlink_id);

    @GET("api/teacher/task/add/teacher/list")
    Call<TeacherListModel> getteacherlist(@HeaderMap Map<String, String> headermap);

    @FormUrlEncoded
    @POST("api/teacher/task/add")
    Call<ResponseBody> addtask(@HeaderMap Map<String, String> headermap,
                               @Field("assignee") String assignee,
                               @Field("standardLink_id") String standardLink_id,
                               @Field("selectedUsers[]") List<Integer> selectedUsers,
                               @Field("teachers[]") List<Integer> teachers,
                               @Field("title") String title,
                               @Field("to_do_list") String to_do_list,
                               @Field("task_date") String task_date,
                               @Field("reminder") String reminder);

    @GET("api/teacher/mytasks/active")
    Call<ResponseBody> getMyActivetask(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/mytasks/completed")
    Call<ResponseBody> getMyCompletedTask(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/tasks/completed")
    Call<ResponseBody> getOtherCompletedTask(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/tasks/active")
    Call<ResponseBody> getOtherActivetask(@HeaderMap Map<String, String> headermap);

    @GET("api/teacher/task/delete/{task_id}")
    Call<ApiSuccessModel> getdeletetask(@HeaderMap Map<String,String> stringStringMap, @Path("task_id") String task_id);

    @FormUrlEncoded
    @POST("api/password/reset")
    Call<ApiSuccessModel> resetpassword(@HeaderMap Map<String,String> headerMap, @Field("mobile_no") String mobile_no,@Field("usergroup") int usergroup);

    @FormUrlEncoded
    @POST("api/password/store")
    Call<ApiSuccessModel> enterop(@HeaderMap Map<String,String> headerMap, @Field("mobile_no") String mobile_no, @Field("password") String password,@Field("usergroup") int usergroup);

    @FormUrlEncoded
    @POST("api/reset/change/password")
    Call<ApiSuccessModel> changepassword(@HeaderMap Map<String,String> stringStringMap,
                                         @Field("mobile_no") String mobile_no,
                                         @Field("oldpassword") String oldpassword,
                                         @Field("newpassword")String newpassword,
                                         @Field("usergroup") int usergroup,
                                         @Field("confirmpassword") String confirmpassword);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("api/teacher/logout/devices")
    Call<ApiSuccessModel> getlogoutalldevices(@Field("email") String email);

    @FormUrlEncoded
    @POST("api/teacher/change/password")
    Call<ApiSuccessModel> changepasswordwithlogin(@HeaderMap Map<String,String> stringStringMap,
                                                  @Field("oldpassword") String oldpassword,
                                                  @Field("newpassword")String newpassword,
                                                  @Field("confirmpassword") String confirmpassword);

    @GET("api/teacher/exam/list")
    Call<ExamListModel> getexamlist(@HeaderMap Map<String,String> headermap);

    @GET("api/teacher/exam/marks/show/{schedule_id}")
    Call<ViewMarkModel> showmarks(@HeaderMap Map<String,String> headermap, @Path("schedule_id")int schedule_id);

    @POST("api/teacher/exam/add/marks/{schedule_id}")
    Call<SuccessApiCallModel> uploadmarks(@HeaderMap Map<String,String> stringStringMap,
                                          @Path("schedule_id")int schedule_id,
                                          @Body AddMarkStudentListModel uploadMarksModel);

    @GET("api/teacher/exam/add/list/{schedule_id}")
    Call<AddMarkStudentListModel> getstudentlistforaddmarks(@HeaderMap Map<String,String> headermap,@Path("schedule_id")int schedule_id);

    @GET("api/teacher/videoroom/invites/{video_conference_id}")
    Call<InvitesModel> getInvites(@HeaderMap Map<String, String> headermap, @Path("video_conference_id") int video_conference_id);

    @GET("api/teacher/videoroom/removeUser/{video_conference_id}")
    Call<ResponseBody> deleteUserfromVideoConference(@HeaderMap Map<String, String> headermap, @Path("video_conference_id") int video_conference_id);

    @FormUrlEncoded
    @POST("api/teacher/videoroom/add/invites/{video_conference_id}")
    Call<ApiSuccessModel> addInvitestoRoom(@HeaderMap Map<String, String> headermap, @Field("students[]") List<Integer> student_ids, @Path("video_conference_id") int video_conference_id);

    @GET("api/teacher/videoroom/list/standard")
    Call<StandardList> getStandardList(@HeaderMap Map<String, String> headermap);


    @GET("api/teacher/videoroom/list/students/{standard_id}")
    Call<StudentList> getStudentList(@HeaderMap Map<String, String> headermap, @Path("standard_id") String standard_id);

    @FormUrlEncoded
    @POST("api/teacher/videoroom/edit/{id}")
    Call<ResponseBody> editRoom(@HeaderMap Map<String, String> headermap,  @Field("description") String description, @Field("standard") String standard, @Field("students[]") List<Integer> students,@Path("id") Integer id
            ,@Field("joining_date") String date,@Field("duration") String duration,@Field("subject") String subject_id
    );

    @GET("api/teacher/videoroom/edit/list/{room_id}")
    Call<EditRoomModel> editroomdata(@HeaderMap Map<String, String> headermap, @Path("room_id") Integer room_id);

    @FormUrlEncoded
    @POST("api/teacher/videoroom/create")
    Call<ResponseBody> createRoom(@HeaderMap Map<String, String> headermap, @Field("name") String name, @Field("description") String description, @Field("standard") String standard, @Field("students[]") List<Integer> students,

                                  @Field("joining_date") String date,@Field("duration") String duration,@Field("subject") String subject_id );

    @GET("api/teacher/videoroom/list/subjects/{standard_id}")
    Call<VideoRoomSubjectModel> getVideoRoomSubjectlist(@HeaderMap Map<String, String> headermap, @Path("standard_id")String standard_id);

    @GET("api/teacher/timetable")
    Call<ResponseBody> gettimetablemodel(@HeaderMap Map<String,String> stringStringMap);

    @GET("api/teacher/video-conference/{slug}/{student_id}")
    Call<JoinRoomModel> getTokentoJoinRoom(@HeaderMap Map<String, String> headerMap, @Path("slug") String slug, @Path("student_id") String student_id);

    @GET("api/teacher/video-conference")
    Call<RoomListModel> getListofRooms(@HeaderMap Map<String, String> headerMap);

    @GET("api/teacher/videoroom/delete/{id}")
    Call<ApiSuccessModel> deletevideoRoom(@HeaderMap Map<String, String> headermap, @Path("id")Integer id);

    @GET("api/teacher/videoroom/status/update/{conference_id}")
    Call<ApiSuccessModel> getvideorecord(@HeaderMap Map<String,String> headermap,@Path("conference_id")int conference_id);

}
