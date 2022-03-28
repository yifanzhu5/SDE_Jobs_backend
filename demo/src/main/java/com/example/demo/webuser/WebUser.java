package com.example.demo.webuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class WebUser implements UserDetails {
    //TODO

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer id;
    private String username;    //用户名
    private String password;    //用户密码
    private String email;       //用户邮箱
    @Enumerated(EnumType.STRING)
    private WebUserRole webUserRole;        //用户角色
    private String image;       //用户头像
    private String lastIp;     //上次登录IP
    private String lastTime;   //上次登录时间
    private Boolean locked = false;
    private Boolean enabled = false;
    @ElementCollection
    //@CollectionTable(name = "fav_list", joinColumns = @JoinColumn(name = "web_user_id"))
    private List<Long> favList;

    public WebUser(String username, String password, String email, WebUserRole webUserRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.webUserRole = webUserRole;
        favList = new ArrayList<>();
        favList.add(0L);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(webUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
