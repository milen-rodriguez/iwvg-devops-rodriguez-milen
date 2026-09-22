package es.upm.miw.devops.domain;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BillablePolicy {

  private static final Map<String, Function<User, String>> REQUIRED_FIELDS =
      Map.of(
          "firstName", User::getFirstName,
          "familyName", User::getFamilyName,
          "email", User::getEmail,
          "identity", User::getIdentity,
          "address", User::getAddress,
          "city", User::getCity,
          "province", User::getProvince,
          "postalCode", User::getPostalCode);

  private BillablePolicy() {}

  public static boolean isBillable(User user) {
    return REQUIRED_FIELDS.values().stream()
        .map(field -> field.apply(user))
        .allMatch(BillablePolicy::hasContent);
  }

  public static Set<String> requiredFieldNames() {
    return REQUIRED_FIELDS.keySet();
  }

  private static boolean hasContent(String value) {
    return value != null && !value.isBlank();
  }
}
