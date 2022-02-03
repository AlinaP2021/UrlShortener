package shortener.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import shortener.models.ShortLink;

import java.util.List;

@Component
public class ShortLinkDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ShortLinkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public ShortLink findByHash(String hash) {
        return jdbcTemplate.query("select * from short_links where hash=?", new Object[]{hash},
                        new BeanPropertyRowMapper<>(ShortLink.class))
                .stream().findAny().orElse(null);
    }

    public ShortLink save(ShortLink shortLink) {
        jdbcTemplate.update("insert into short_links (hash, original_url, start_date, life_span, redirects_number) values (?, ?, ?, ?, ?)",
                shortLink.getHash(), shortLink.getOriginalUrl(), shortLink.getStartDate(), shortLink.getLifeSpan(),
                shortLink.getRedirectsNumber());
        return findByHash(shortLink.getHash());
    }

    public List<ShortLink> showAllShortLinks() {
        return jdbcTemplate.query("select * from short_links", new BeanPropertyRowMapper<>(ShortLink.class));
    }

    public void delete(String hash) {
        jdbcTemplate.update("delete from short_links where hash=?", hash);
    }

    public void update(String hash, ShortLink shortLink) {
        jdbcTemplate.update("update short_links set life_span=? where hash=?",
                shortLink.getLifeSpan(), hash);
    }

    public void increaseRedirectsNumber(ShortLink shortLink) {
        jdbcTemplate.update("update short_links set redirects_number=? where id=?",
                shortLink.getRedirectsNumber() + 1, shortLink.getId());
    }
}
