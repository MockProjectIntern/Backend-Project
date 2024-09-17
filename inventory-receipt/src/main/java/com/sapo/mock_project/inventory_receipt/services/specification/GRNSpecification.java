package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.GetListGRNRequest;
import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp định nghĩa các điều kiện lọc cho đối tượng {@link GRN} sử dụng JPA Criteria API.
 * Các điều kiện lọc được dựa trên thông tin từ đối tượng {@link GetListGRNRequest}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNSpecification implements Specification<GRN> {

    private GetListGRNRequest request;

    @Override
    public Predicate toPredicate(Root<GRN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Lấy các thông tin lọc từ đối tượng request
        String keyword = request.getKeyword();
        String status = request.getStatus();
        String receivedStatus = request.getReceivedStatus();
        String paymentStatus = request.getPaymentStatus();
        String returnStatus = request.getReturnStatus();
        String refundStatus = request.getRefundStatus();
        String supplierId = request.getSupplierId();
        String tags = request.getTags();
        String note = request.getNote();

        LocalDateTime createdDate = DateUtils.getDateTime(request.getCreatedDate());
        LocalDateTime expectedDeliveryAt = DateUtils.getDateTime(request.getExpectedDeliveryAt());
        LocalDateTime receivedAt = DateUtils.getDateTime(request.getReceivedAt());
        LocalDateTime cancelledAt = DateUtils.getDateTime(request.getCancelledAt());
        LocalDateTime paymentAt = DateUtils.getDateTime(request.getPaymentAt());
        LocalDateTime endedAt = DateUtils.getDateTime(request.getEndedAt());

        // Điều kiện lọc theo từ khóa (keyword)
        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();

            Predicate idPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("id")),
                    String.format("%%%s%%", keywordUpper));
            Predicate notePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("note")),
                    String.format("%%%s%%", keywordUpper));
            Predicate tagsPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")),
                    String.format("%%%s%%", keywordUpper));


            predicates.add(criteriaBuilder.or(idPredicate, notePredicate,tagsPredicate));
        }

        // Điều kiện lọc theo trạng thái chung (status)
        if (status != null && !status.isEmpty()) {
            Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
            predicates.add(statusPredicate);
        }

        // Điều kiện lọc theo trạng thái nhận hàng (receivedStatus)
        if (receivedStatus != null && !receivedStatus.isEmpty()) {
            Predicate receivedStatusPredicate = criteriaBuilder.equal(root.get("receivedStatus"), receivedStatus);
            predicates.add(receivedStatusPredicate);
        }

        // Điều kiện lọc theo trạng thái thanh toán (paymentStatus)
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            Predicate paymentStatusPredicate = criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus);
            predicates.add(paymentStatusPredicate);
        }

        // Điều kiện lọc theo trạng thái trả hàng (returnStatus)
        if (returnStatus != null && !returnStatus.isEmpty()) {
            Predicate returnStatusPredicate = criteriaBuilder.equal(root.get("returnStatus"), returnStatus);
            predicates.add(returnStatusPredicate);
        }

        // Điều kiện lọc theo trạng thái hoàn tiền (refundStatus)
        if (refundStatus != null && !refundStatus.isEmpty()) {
            Predicate refundStatusPredicate = criteriaBuilder.equal(root.get("refundStatus"), refundStatus);
            predicates.add(refundStatusPredicate);
        }

        // Điều kiện lọc theo nhà cung cấp (supplierId)
        if (supplierId != null && !supplierId.isEmpty()) {
            Predicate supplierPredicate = criteriaBuilder.equal(root.get("supplier").get("id"), supplierId);
            predicates.add(supplierPredicate);
        }

        // Điều kiện lọc theo tags
        if (tags != null && !tags.isEmpty()) {
            Predicate tagsPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")),
                    String.format("%%%s%%", tags.toUpperCase()));
            predicates.add(tagsPredicate);
        }

        // Điều kiện lọc theo ghi chú (note)
        if (note != null && !note.isEmpty()) {
            Predicate notePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("note")),
                    String.format("%%%s%%", note.toUpperCase()));
            predicates.add(notePredicate);
        }

        // Điều kiện lọc theo ngày tạo (createdDate)
        if (createdDate != null) {
            Predicate createdDatePredicate = criteriaBuilder.equal(root.get("createdAt"), createdDate);
            predicates.add(createdDatePredicate);
        }

        // Điều kiện lọc theo ngày dự kiến giao hàng (expectedDeliveryAt)
        if (expectedDeliveryAt != null) {
            Predicate expectedDeliveryAtPredicate = criteriaBuilder.equal(root.get("expectedDeliveryAt"), expectedDeliveryAt);
            predicates.add(expectedDeliveryAtPredicate);
        }

        // Điều kiện lọc theo ngày nhận hàng (receivedAt)
        if (receivedAt != null) {
            Predicate receivedAtPredicate = criteriaBuilder.equal(root.get("receivedAt"), receivedAt);
            predicates.add(receivedAtPredicate);
        }

        // Điều kiện lọc theo ngày hủy (cancelledAt)
        if (cancelledAt != null) {
            Predicate cancelledAtPredicate = criteriaBuilder.equal(root.get("cancelledAt"), cancelledAt);
            predicates.add(cancelledAtPredicate);
        }

        // Điều kiện lọc theo ngày thanh toán (paymentAt)
        if (paymentAt != null) {
            Predicate paymentAtPredicate = criteriaBuilder.equal(root.get("paymentAt"), paymentAt);
            predicates.add(paymentAtPredicate);
        }

        // Điều kiện lọc theo ngày kết thúc (endedAt)
        if (endedAt != null) {
            Predicate endedAtPredicate = criteriaBuilder.equal(root.get("endedAt"), endedAt);
            predicates.add(endedAtPredicate);
        }

        // Kết hợp tất cả các điều kiện bằng phép AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
