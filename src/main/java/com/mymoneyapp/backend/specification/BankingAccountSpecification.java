package com.mymoneyapp.backend.specification;

import com.mymoneyapp.backend.domain.BankingAccount;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction(value = {}, and = {
        @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
        @Spec(path = "email", params = "email", spec = LikeIgnoreCase.class),
})
public interface BankingAccountSpecification extends Specification<BankingAccount> {
}
