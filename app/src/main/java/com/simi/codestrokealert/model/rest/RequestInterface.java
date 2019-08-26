package com.simi.codestrokealert.model.rest;

import com.simi.codestrokealert.model.CaseAssessments;
import com.simi.codestrokealert.model.CaseAssessmentsResponse;
import com.simi.codestrokealert.model.CaseCompleteManagements;
import com.simi.codestrokealert.model.CaseEdResponse;
import com.simi.codestrokealert.model.CaseEds;
import com.simi.codestrokealert.model.CaseHistories;
import com.simi.codestrokealert.model.CaseHistoriesResponse;
import com.simi.codestrokealert.model.CaseManagements;
import com.simi.codestrokealert.model.CaseManagmentsResponse;
import com.simi.codestrokealert.model.CaseRadiologies;
import com.simi.codestrokealert.model.CaseRadiologiesResponse;
import com.simi.codestrokealert.model.Cases;
import com.simi.codestrokealert.model.Cases1;
import com.simi.codestrokealert.model.CasesResponse;
import com.simi.codestrokealert.model.Hospital;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestInterface {


    @DELETE("cases/{id}/")
    Call<ResponseBody> deleteCase(@Path("id") int case_id);

    @POST("cases/add/")
    Call<Cases> addCase(@Body Cases cases);

    @GET("hospital_list.json")
    Call<List<Hospital>> getHospitals();

    @PUT("case_assessments/{id}/edit/")
    Call<CaseAssessments> sendCaseAssessments(@Path("id") int case_id, @Body CaseAssessments caseAssessments);

    @PUT("cases/{id}/edit/")
    Call<Cases1> sendCases(@Path("id") int case_id, @Body Cases1 caseAssessments);

    @GET("cases/view/")
    Call<CasesResponse> getCases();

    @GET("case_eds/{id}/view/")
    Call<CaseEdResponse> getCaseEd(@Path("id") int case_id);

    @PUT("case_eds/{id}/edit/")
    Call<CaseEds> updateCaseEd(@Path("id") int case_id, @Body CaseEds caseEds);

    @GET("cases/{id}/view/")
    Call<CasesResponse> getPatient(@Path("id") int case_id);

    @PUT("cases/{id}/edit/")
    Call<Cases> updatePatientDetails(@Path("id") int case_id, @Body Cases cases);

    @GET("case_histories/{id}/view/")
    Call<CaseHistoriesResponse> getCaseHistories(@Path("id") int case_id);

    @PUT("case_histories/{id}/edit/")
    Call<CaseHistories> updateCase(@Path("id") int case_id, @Body CaseHistories caseHistories);

    @GET("case_assessments/{id}/view/")
    Call<CaseAssessmentsResponse> getCaseAssessments(@Path("id") int case_id);

    @PUT("case_assessments/{id}/edit/")
    Call<CaseAssessments> updateCaseAssessment(@Path("id") int case_id, @Body CaseAssessments caseAssessments);

    @GET("case_radiologies/{id}/view/")
    Call<CaseRadiologiesResponse> getCaseRadiologies(@Path("id") int case_id);

    @PUT("case_radiologies/{id}/edit/")
    Call<CaseRadiologies> updateCaseRadiologies(@Path("id") int case_id, @Body CaseRadiologies caseRadiologies);

    @GET("case_managements/{id}/view/")
    Call<CaseManagmentsResponse> getCaseManagments(@Path("id") int case_id);

    @PUT("case_managements/{id}/edit/")
    Call<CaseManagements> updateCaseManagements(@Path("id") int case_id, @Body CaseManagements caseManagements);

    @PUT("cases/{id}/edit/")
    Call<CaseCompleteManagements> completeCaseManagements(@Path("id") int case_id, @Body CaseCompleteManagements caseManagements);

}
