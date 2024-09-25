package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.GetListGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.UpdateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.grn.GRNService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Bộ điều khiển cho các hoạt động liên quan đến phiếu nhập kho (GRN).
 * Cung cấp các API để tạo và xử lý phiếu nhập kho.
 */
@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.GRN)
@RequiredArgsConstructor
@Slf4j
public class GRNController {
    private final GRNService grnService;

    /**
     * Tạo một phiếu nhập kho mới.
     *
     * @param request Yêu cầu chứa thông tin của phiếu nhập kho cần tạo.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả tạo phiếu nhập kho.
     */
    @PostMapping("/create.json")
    public ResponseEntity<ResponseObject<Object>> createGRN(@Valid @RequestBody CreateGRNRequest request) {
        return grnService.createGRN(request);
    }

    /**
     * Lấy thông tin chi tiết của một phiếu nhập kho theo ID.
     *
     * @param id ID của phiếu nhập kho cần lấy thông tin.
     * @return ResponseEntity chứa thông tin chi tiết về phiếu nhập kho.
     */
    @GetMapping("/detail.json/{id}")
    public ResponseEntity<ResponseObject<Object>> getGRNById(@PathVariable("id") String id) {
        return grnService.getGRNById(id);
    }

    /**
     * Cập nhật thông tin của một phiếu nhập kho theo ID.
     *
     * @param id      ID của phiếu nhập kho cần cập nhật.
     * @param request Yêu cầu chứa thông tin cập nhật của phiếu nhập kho.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả cập nhật phiếu nhập kho.
     */
    @PutMapping("/update.json/{id}")
    public ResponseEntity<ResponseObject<Object>> updateGRN(@PathVariable String id,
                                                            @Valid @RequestBody UpdateGRNRequest request) {
        return grnService.updateGRN(id, request);
    }

    /**
     * Xóa một phiếu nhập kho theo ID.
     *
     * @param id ID của phiếu nhập kho cần xóa.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả xóa phiếu nhập kho.
     */
    @DeleteMapping("/delete.json/{id}")
    public ResponseEntity<ResponseObject<Object>> deleteGRN(@PathVariable("id") String id) {
        return grnService.deleteGRN(id);
    }

    /**
     * Lấy danh sách các phiếu nhập kho theo các điều kiện lọc.
     *
     * @param request Yêu cầu chứa thông tin các điều kiện lọc.
     * @param page    Trang hiện tại.
     * @param size    Số lượng bản ghi trên mỗi trang.
     * @return ResponseEntity chứa thông tin danh sách các phiếu nhập kho.
     */
    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterGRN(@RequestBody GetListGRNRequest request,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.GRN, httpServletRequest);

        return grnService.filterGRN(request, filterParams, page, size);
    }

    /**
     * Nhập phiếu nhập kho.
     *
     * @param id ID của phiếu nhập kho cần nhập.
     * @return ResponseEntity chứa thông tin phản hồi về kết quả nhập phiếu nhập kho.
     */
    @PutMapping("/import.json/{id}")
    public ResponseEntity<ResponseObject<Object>> importGRN(@PathVariable String id) {
        return grnService.importGRN(id);
    }

    @GetMapping("/supplier-all.json/{supplierId}")
    public ResponseEntity<ResponseObject<Object>> getAllBySupplier(@PathVariable String supplierId,
                                                                   @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return grnService.getAllBySupplier(supplierId, page, size);
    }
}
