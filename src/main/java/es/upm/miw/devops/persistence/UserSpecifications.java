package es.upm.miw.devops.persistence;

import es.upm.miw.devops.domain.BillablePolicy;
import es.upm.miw.devops.domain.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

  private UserSpecifications() {}

  public static Specification<User> withFilters(Boolean billable, Boolean active) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      addActiveFilter(predicates, root, criteriaBuilder, active);
      addBillableFilter(predicates, root, criteriaBuilder, billable);
      return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    };
  }

  private static void addActiveFilter(
      List<Predicate> predicates,
      Root<User> root,
      CriteriaBuilder criteriaBuilder,
      Boolean active) {
    if (active != null) {
      predicates.add(criteriaBuilder.equal(root.get("active"), active));
    }
  }

  private static void addBillableFilter(
      List<Predicate> predicates,
      Root<User> root,
      CriteriaBuilder criteriaBuilder,
      Boolean billable) {
    if (billable == null) {
      return;
    }

    Predicate billablePredicate =
        criteriaBuilder.and(
            BillablePolicy.requiredFieldNames().stream()
                .map(field -> hasContent(root, criteriaBuilder, field))
                .toArray(Predicate[]::new));
    predicates.add(billable ? billablePredicate : criteriaBuilder.not(billablePredicate));
  }

  private static Predicate hasContent(
      Root<User> root, CriteriaBuilder criteriaBuilder, String field) {
    Path<String> value = root.get(field);
    return criteriaBuilder.and(
        criteriaBuilder.isNotNull(value),
        criteriaBuilder.notEqual(criteriaBuilder.trim(value), ""));
  }
}
