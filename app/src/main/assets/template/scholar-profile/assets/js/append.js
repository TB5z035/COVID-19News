function getEmails(data) {
  var emails = [];
  if ('emails_u' in data.profile) {
    data.profile.emails_u.map((x) => (emails.push(x.address)));
  }
  if (data.profile.email && data.profile.email !== "") {
    emails.push(data.profile.email);
  }
  if (data.profile.email_cr && data.profile.email_cr !== "") {
    emails.push(data.profile.email_cr);
  }
  return emails;
}

function getMapper(data) {
  var mapper = {
    avatar: data.avatar,
    name: data.name,
    name_zh: data.name_zh,
    activity: data.indices.activity,
    citations: data.indices.citations,
    diversity: data.indices.diversity,
    gindex: data.indices.gindex,
    hindex: data.indices.hindex,
    newStar: data.indices.newStar,
    risingStar: data.indices.risingStar,
    sociability: data.indices.sociability,
    affiliation: data.profile.affiliation,
    affiliation_zh: data.profile.affiliation_zh,
    bio: data.profile.bio,
    edu: data.profile.edu,
    emails_u: getEmails(data),
    homepage: data.profile.homepage,
    // note: data.profile.note,
    phone: data.profile.phone,
    position: data.profile.position,
    work: data.profile.work,
    tags: data.tags,
    is_passedaway: data.is_passedaway ? "追忆学者" : ""
  };
  for (var k in mapper) {
    mapper[k] = mapper[k] == null ? "" : mapper[k];
  }
  return mapper;
}

function getScholar(idx, callback) {
  var url = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=" + idx;
  console.log(url);
  (fetch(url)
    .then(response => response.json())
    .then(data => callback(data)));
}

function list2li(list) {
  if (list === null) {
    return "";
  } else if (typeof (list) === 'string') {
    return list;
  }
  return '<ul class="alt">' + list.map((x) => "<li>" + x + "</li>").join(' ') + '</ul>';
}

function replaceAll(src, pattern, substitute) {
  return src.split(pattern).join(substitute);
}

function appendScholar(data, idx) {
  var mapper = getMapper(data);
  var item = `
          <a href={{href}}
            <div class="image fit">
							<img src={{avatar}} alt="">
						</div>
						<p class="caption">
							{{name}} {{name_zh}}
            </p>
          </a>
  `;

  item = replaceAll(item, '{{href}}', "?id=" + idx);
  ['avatar', 'name', 'name_zh'].map((k) => {
    item = replaceAll(item, '{{' + k + '}}', mapper[k]);
  });
  console.log(item);

  var itemContainer = document.createElement('div');
  itemContainer.setAttribute("class", "video col");
  itemContainer.innerHTML = item;
  document.getElementsByClassName('flex flex-3')[0].appendChild(itemContainer);
}

function processJson(data, i) {
  if (data.message != "success") {
    return;
  }
  if (i === null) {
    document.body.innerHTML = `
      <div id="main">
      <section class="wrapper ">
        <div class="inner">
          <header class="align-center">
            <h2>学者列表</h2>
            <p>...</p>
          </header>
          <div class="flex flex-3">
          </div>
        </div>
      </section>
    </div>
  `;
    data.data.map((x, idx) => appendScholar(x, idx));
  } else {
    displayProfile(data.data[i]);
  }
}

function displayProfile(data) {
  var mapper = getMapper(data);
  var item = document.body.innerHTML;
  for (var k in mapper) {
    if (k == 'emails_u' || k == 'tags') {
      var sublist = list2li(mapper[k]);
      item = replaceAll(item, '[[' + k + ']]', sublist);
    } else if (k == 'bio' || k == 'work' || k == 'homepage') {
      var sublist = list2li(mapper[k].split('\n'));
      item = replaceAll(item, '[[' + k + ']]', sublist);
    } else {
      item = replaceAll(item, '{{' + k + '}}', mapper[k]);
    }
  }
  document.body.innerHTML = item;
}


